package com.royal.app.chatengine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

@ApplicationScope
@Component
public class ChatSessionMgt {

  private Map<String, Map<String, Object>> chatSessions = new HashMap<String, Map<String, Object>>();

  private Map<String, String> agentAssistance = new HashMap<String, String>();
  
  
  
  public Map<String, String> getAgentAssistance() {
    return agentAssistance;
  }

  public void setAgentAssistance(Map<String, String> agentAssistance) {
    this.agentAssistance = agentAssistance;
  }

  public Map<String, Map<String, Object>> getChatSessions() {
    return chatSessions;
  }

  public void setChatSessions(Map<String, Map<String, Object>> chatSessions) {
    this.chatSessions = chatSessions;
  }
  
  
}
