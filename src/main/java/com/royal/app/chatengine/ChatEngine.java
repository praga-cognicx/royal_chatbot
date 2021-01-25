package com.royal.app.chatengine;

import java.time.format.DateTimeFormatter;
import org.alicebot.ab.Chat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
public class ChatEngine implements SchedulingConfigurer {

  
  private static final Logger logger = LoggerFactory.getLogger(ChatEngine.class);
  private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
  
  @Autowired
  Chat chatSession;
  
  
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
             System.out.println("Chat Engine Running..."+ chatSession.multisentenceRespond("Hi"));
             
           } catch (Exception e1) {
             e1.printStackTrace();
           }
         
       }, "0/10 * * * * ?");
     } catch (Exception e) {
     logger
       .error("ChatEngine Application Exception:: Scheduling Not Started/DB not connected. Method:configureTasks()", e);
     }
   }
 
   public static void main(String []q) {
     
     ChatEngine ce = new ChatEngine();
     ce.configureTasks(new ScheduledTaskRegistrar());
   }

 
}
