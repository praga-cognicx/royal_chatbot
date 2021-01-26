package com.royal.app.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.royal.app.dao.ChatDemoDAO;
import com.royal.app.services.ChatDemoService;

@Service
public class ChatDemoServiceImpl implements ChatDemoService{
  
  @Autowired
  ChatDemoDAO chatDemoDAO;

  private static final Logger logger = LoggerFactory.getLogger(ChatDemoServiceImpl.class);

  @Override
  public String getKeyValuePair(String key, String id) throws Exception {
    
    return chatDemoDAO.getKeyValuePair(key, id);
    
  }
  
  
  
}
