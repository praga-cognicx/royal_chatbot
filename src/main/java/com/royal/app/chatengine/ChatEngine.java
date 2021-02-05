package com.royal.app.chatengine;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.alicebot.ab.Chat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.web.client.RestTemplate;
import com.royal.app.message.request.AgentAssign;
import com.royal.app.message.request.SendMessage;
import com.royal.app.messagerpeople.MessagerPeopleAPISetting;
import com.royal.app.services.ChatDemoService;

@Configuration
public class ChatEngine implements SchedulingConfigurer {

  
  private static final Logger logger = LoggerFactory.getLogger(ChatEngine.class);
  private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
  
  @Autowired
  Chat chatSession;
  
  @Autowired
  RestTemplate restTemplate;
  
  @Autowired
  ChatDemoService chatDemoService;
  
  @Autowired
  MessagerPeopleAPISetting messagerPeopleAPISetting;
  
  @Autowired
  ChatSessionMgt chatSessionMgt;
  
  /**
  * 
  * Below you can find the example patterns from the spring forum:

 * "0 0 * * * *" = the top of every hour of every day.
 * "10 * * * * *" = every ten seconds.
 * "0 0 8-10 * * *" = 8, 9 and 10 o'clock of every day.
 * "0 0 8,10 * * *" = 8 and 10 o'clock of every day.
 * "0 0/30 8-10 * * *" = 8:00, 8:30, 9:00, 9:30 and 10 o'clock every day.
 * "0 0 9-17 * * MON-FRI" = on the hour nine-to-five weekdays
 * "0 0 0 25 12 ?" = every Christmas Day at midnight
 *Cron expression is represented by six fields:

 second, minute, hour, day of month, month, day(s) of week
 (*) means match any

 *X means "every X"

 *? ("no specific value") - useful when you need to specify something in one of the two fields
 * in which the character is allowed, but not the other. For example, if I want my trigger to 
 * fire on a particular day of the month (say, the 10th), but I don't care what day of the week
 *  that happens to be, I would put "10" in the day-of-month field and "?" in the day-of-week field.
  * @throws Exception */

  /* @Scheduled(cron = "0 3 * * * ?", zone = "IST")
   public void scheduleTaskWithCronExpression() throws Exception {
     
     System.out.println("Schedular running...");
   }*/
   private final int POOL_SIZE = 10;
   public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
     ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();

     threadPoolTaskScheduler.setPoolSize(POOL_SIZE);
     threadPoolTaskScheduler.setThreadNamePrefix("my-scheduled-task-pool-");
     threadPoolTaskScheduler.initialize();
     scheduledTaskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
     try {
         
       /**Chat Scheduler */
       scheduledTaskRegistrar.addCronTask(() -> {
           try {
             System.out.println("Chat Engine Running...");
             String url = messagerPeopleAPISetting.getMessagengerPeopleUrl()+"/ticket?apikey="+messagerPeopleAPISetting.getMessagengerPeopleApikey()+"&num_chats=1&customfields=1&last_chat=1&notes=0&status=1&newsletter=1&order=waiting_since&asc=1&offset=0&limit=150";
             ResponseEntity<String> entity = restTemplate.getForEntity(url, String.class);
            if(200 == entity.getStatusCodeValue()) {
              JSONObject json = new JSONObject(entity.getBody());
              JSONArray ticketList = json.getJSONArray("tickets");
              for(int i=0; i < ticketList.length(); i++) {
                JSONObject ticketObject = ticketList.getJSONObject(i);
                JSONArray chatList = ticketObject.getJSONArray("chats");
                /*Map<String, Map<String,Object>> chatSessions = new HashMap<String, Map<String,Object>>();
                Map<String,Object> chatsList = new HashMap<String,Object>();
                chatsList.put("chat", chatList);
                chatSessions.put(ticketObject.getString("id"), chatsList);
                chatSessionMgt.setChatSessions(chatSessions);*/
                
                for(int cIndex=0; cIndex < chatList.length(); cIndex++) {
                  JSONObject chatObject = chatList.getJSONObject(cIndex);
                  Map<String, String> agentAssistance = chatSessionMgt.getAgentAssistance();
                  if(agentAssistance != null && !agentAssistance.isEmpty() && "Agent assistance".contains(chatObject.getString("chat").toString()) || chatObject.getString("chat").toString().contains("assistance") || chatObject.getString("chat").toString().contains("3")) {
                    agentAssistance.put(ticketObject.getString("user_id"), "true");
                    chatSessionMgt.setAgentAssistance(agentAssistance);
                  }
                if(!(boolean) chatObject.get("outgoing")) {
                  new Thread(() -> {
                    //Do whatever
                    String sendRes = "";
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setApikey(messagerPeopleAPISetting.getMessagengerPeopleApikey());
                    try {
                      if(chatObject.get("chat").toString().contains("C-")) {
                        ResponseEntity<String> agentEntity = restTemplate.getForEntity(messagerPeopleAPISetting.getMessagengerPeopleUrl()+"//agent?open_tickets=1&apikey="+messagerPeopleAPISetting.getMessagengerPeopleApikey(), String.class);
                        if(200 == agentEntity.getStatusCodeValue()) {
                          JSONObject agentJson = new JSONObject(agentEntity.getBody());
                          JSONArray agents = agentJson.getJSONObject("agents").names();
                          for(int agentIndex=0; agentIndex < agents.length(); agentIndex++) {
                            JSONObject agentObject = agentJson.getJSONObject("agents").getJSONObject((String) agents.get(agentIndex));
                            if("1".equalsIgnoreCase(agentObject.getString("active")) 
                                && agentObject.getString("roles").contains("chatAgent")) {
                              JSONArray channelId = agentObject.getJSONArray("channels");
                              JSONArray agentTicketObject =  agentObject.getJSONObject("tickets").getJSONObject(channelId.getString(0)).names();   
                              
                              for(int agentTIndex=0; agentTIndex < agentTicketObject.length(); agentTIndex++) {
                                if(ticketObject.getString("id").equalsIgnoreCase(agentTicketObject.get(agentTIndex).toString())){
                                AgentAssign agentAssign = new AgentAssign();
                                agentAssign.setApikey(messagerPeopleAPISetting.getMessagengerPeopleApikey());
                                agentAssign.setAgent_id(agentObject.getString("id"));
                                String [] batchIds = {ticketObject.getString("id")};
                                agentAssign.setBatch_ids(batchIds);
                                restTemplate.put(messagerPeopleAPISetting.getMessagengerPeopleUrl()+"/ticket", agentAssign);
                                }
                                }
                            }
                          
                          }
                        }
                        
                        
                      } else {
                        String res = chatMessage((String) chatObject.get("chat"), ticketObject.getString("user_id"));
                        if(!"NO".equals(res)) {
                        sendMessage.setMessage(chatMessage((String) chatObject.get("chat"), ticketObject.getString("user_id")));
                        sendMessage.setId(ticketObject.getString("user_id"));
                        try {
                        ResponseEntity<String>  sendChatResult = restTemplate.postForEntity(messagerPeopleAPISetting.getMessagengerPeopleUrl()+"/chat", sendMessage, String.class);
                        if(412 == sendChatResult.getStatusCodeValue()) {
                          sendRes = sendChatResult.getBody();
                        }
                        }catch(Exception ex) {
                          System.out.println("Exception ChatEngine Chat Send::"+ex);
                        }
                        }
                      }
                      
                      System.out.println("Chat sent::"+sendRes);
                    } catch (JSONException e) {
                      // TODO Auto-generated catch block
                      e.printStackTrace();
                    } catch (Exception e) {
                      // TODO Auto-generated catch block
                      e.printStackTrace();
                    }
                   
                  }).start();
                }       
                }
                
              }             
            }
           
             
           } catch (Exception e1) {
             e1.printStackTrace();
           }
         
       }, "0/3 * * * * ?");
     } catch (Exception e) {
     logger
       .error("ChatEngine Application Exception:: Scheduling Not Started/DB not connected. Method:configureTasks()", e);
     }
   }
   
   
   public String chatMessage(String request, String userId) throws Exception {
     String response = "";
     if("Hi".equalsIgnoreCase(request) || request.contains("TID")) {
       response = "Welcome to BOT Service. How can i help?\n\n*1.* Account Balance *\u20b9*\n*2.* Account Usage *\u20b9*\n*3.* Agent assistance \n*4.* Exit \n\nPlease type the *option number* to proceed";
     } else if("Balance".equalsIgnoreCase(request)||"1".equalsIgnoreCase(request)) {
       response = "Please give me your customer ID: Format(B-XXXX)";
     } else if("Usage".equalsIgnoreCase(request)||"2".equalsIgnoreCase(request)) {
       response = "Please give me your customer ID: Format(U-XXXX)";
     } else if(request.contains("B-")) {
       response = chatDemoService.getKeyValuePair("Balance", request.split("-")[1]); 
       if(!response.isEmpty()) {
         response = "Your Balance is RS " +response;
       } else {
         response = "Balance not available. Please enter valid customer ID: Format(U-XXXX)";
       }
     } else if(request.contains("U-")) {
       response = chatDemoService.getKeyValuePair("Usage", request.split("-")[1]); 
       if(!response.isEmpty()) {
         response = "Your Usgae is RS " +response;
       } else {
         response = "Usage not available. Please enter valid customer ID: Format(U-XXXX)";
       }
     } else if("Agent assistance".contains(request) || "assistance".contains(request) || "3".equalsIgnoreCase(request)){
       response = "Please give me your customer ID: Format(C-XXXX)";
     } else if("Exit".contains(request) || "4".equalsIgnoreCase(request)){
       response = "Thank You!!!";
     } else {   
       Map<String, String> agentAssistance = chatSessionMgt.getAgentAssistance();
       if(agentAssistance.get(userId) == null || agentAssistance.get(userId).equalsIgnoreCase("false")) {
         response = chatSession.multisentenceRespond(request);
       } else {
         response = "NO";
       }
     }
     
    return response;
     
   }
 
 
}
