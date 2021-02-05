package com.royal.app.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.apache.http.HttpRequest;
import org.apache.http.params.HttpParams;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.royal.app.message.request.LoginForm;
import com.royal.app.message.request.SendMessage;
import com.royal.app.messagerpeople.MessagerPeopleAPISetting;
@RestController
@CrossOrigin
@RequestMapping("/api")
public class ChatBotController {
  
  @Autowired
  RestTemplate restTemplate;
  
  @Autowired
  MessagerPeopleAPISetting messagerPeopleAPISetting;
 
  private Logger logger = Logger.getLogger(ChatBotController.class);
   
  @PostMapping("/login")
  public ResponseEntity<Object> login(@Valid @RequestBody LoginForm loginRequest) throws Exception {
    JSONObject json = null;
    ResponseEntity<String> entity = restTemplate.postForEntity(messagerPeopleAPISetting.getMessagengerPeopleUrl()+"/login", loginRequest, String.class);
    if(200 == entity.getStatusCodeValue()) {
      json = new JSONObject(entity.getBody());
      json.put("apikey", messagerPeopleAPISetting.getMessagengerPeopleApikey());
    }
    return ResponseEntity.ok(json.toString());
  }
  
  @PostMapping("/chat")
  public ResponseEntity<Object> sendChatDetails(@Valid @RequestBody SendMessage sendMessage) throws Exception {
    sendMessage.setApikey(messagerPeopleAPISetting.getMessagengerPeopleApikey());
    Object ob = restTemplate.postForObject(messagerPeopleAPISetting.getMessagengerPeopleUrl()+"/chat", sendMessage, Object.class);
    return ResponseEntity.ok(ob);
  }
  
  @GetMapping("/chat")
  public ResponseEntity<Object> getChatDetails(HttpServletRequest request) throws Exception {
    String requestParam = request.getQueryString();
    requestParam = "?"+ requestParam + "&apikey=" +messagerPeopleAPISetting.getMessagengerPeopleApikey();
    ResponseEntity<String> entity = restTemplate.getForEntity(messagerPeopleAPISetting.getMessagengerPeopleUrl()+"/chat"+requestParam, String.class);
    return ResponseEntity.ok(entity.getBody());
  }
 
  @GetMapping("/ticket/{id}")
  public ResponseEntity<Object> getTicketDetails(@PathVariable("id") String id, HttpServletRequest request) throws Exception {
    String requestParam = request.getQueryString();
    requestParam = "/"+ (id != null && id != "0" || id != "" ? id: "") + "?" + requestParam + "&apikey="+ messagerPeopleAPISetting.getMessagengerPeopleApikey();
    ResponseEntity<String> entity = restTemplate.getForEntity(messagerPeopleAPISetting.getMessagengerPeopleUrl()+"/ticket"+requestParam, String.class);
    return ResponseEntity.ok(entity.getBody());
  }
  
  
  public static void main(String []s) {
    LoginForm loginRequest = new LoginForm();
   
  /*  loginRequest.setPassword("Welcome@123");
    loginRequest.setUsername("Greeshma.Puthan@Cognicx.com");*/
    RestTemplate restTemplate = new RestTemplate();
  /* Object ob = restTemplate.postForObject("https://rest.messengerpeople.com/api/v14/login", loginRequest, Object.class);
   String sendRes = "";*/
   SendMessage sendMessage = new SendMessage();
   sendMessage.setApikey("5542578fe63ed66f179766924e4932ee_42789_7aa541891179f227c8e4eff54");
   sendMessage.setMessage("Hi Welcome");
   sendMessage.setId("918220505534");
   Object ob = restTemplate.postForObject("https://rest.messengerpeople.com/api/v14"+"/chat", sendMessage, Object.class);

    System.out.println("Response::"+ob.toString());
  }
  
  
}
