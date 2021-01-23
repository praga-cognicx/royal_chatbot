package com.royal.app.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.royal.app.message.request.LoginForm;
import com.royal.app.message.request.SendMessage;
import com.royal.app.message.response.GenericResponse;
@RestController
@RequestMapping("/chatbot")
public class ChatBotController {
  
  @Autowired
  RestTemplate restTemplate;

  private Logger logger = Logger.getLogger(ChatBotController.class);
   
  @PostMapping("/login")
  public ResponseEntity<Object> login(@Valid @RequestBody LoginForm loginRequest, HttpServletRequest request) throws Exception {
    Object ob = restTemplate.postForObject("https://rest.messengerpeople.com/api/v14/login", loginRequest, Object.class);
    System.out.println(ob);
    return ResponseEntity.ok(ob);
  }
  
  @PostMapping("/sendmessage")
  public ResponseEntity<Object> userchatSendMessage(@Valid @RequestBody SendMessage sendMessage, HttpServletRequest request) throws Exception {
    Object ob = restTemplate.postForObject("https://rest.messengerpeople.com/api/v14/chat", sendMessage, Object.class);
    System.out.println(ob);
    return ResponseEntity.ok(ob);
  }
  
  public static void main(String []s) {
    LoginForm loginRequest = new LoginForm();
    loginRequest.setApikey("ea1114db7440b2f77b4b0062a8bf7234_41832_0c146e5811c0dc04a990955d5");
    loginRequest.setPassword("balaji.1992cs@gmail.com");
    loginRequest.setUsername("asd@#Ba12");
    RestTemplate restTemplate = new RestTemplate();
   Object ob = restTemplate.postForObject("https://rest.messengerpeople.com/api/v14/login", loginRequest, Object.class);
   
     
    System.out.println("Response::"+ob.toString());
  }
  
  
}
