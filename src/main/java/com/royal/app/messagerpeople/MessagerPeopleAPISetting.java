package com.royal.app.messagerpeople;

import java.util.Map;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MessagerPeopleAPISetting {
  
  @Value("${chat.app.messagengerPeopleApikey}")
  private String messagengerPeopleApikey;
  
  @Value("${chat.app.messagengerPeopleUrl}")
  private String messagengerPeopleUrl;
  
  @Value("${chat.app.version}")
  private String version;
  
  
  public String getMessagengerPeopleApikey() {
    return messagengerPeopleApikey;
  }


  public String getMessagengerPeopleUrl() {
    return messagengerPeopleUrl;
  }

  public String getVersion() {
    return version;
  }

  public String getURL(String urlName) {
    Map<String, String> urls = new HashedMap<>();
    
    /**LOGIN*/
    urls.put("LOGIN_POST", "/login");
    urls.put("LOGINCODE_POST", "/login/code");
    urls.put("LOGIN_DELETE", "/login");
    /**USER*/
    urls.put("USER_DETAILS_GET", "/user/{id}");
    urls.put("USER_DATA_PUT", "/user/{id}");
    urls.put("USER_UPLOAD_CSV_POST", "/user/csv");
    urls.put("USER_DELETE_DELETE", "/user/{id}");
    urls.put("USER_START_PUT", "/user/{id}/start");
    urls.put("USER_STOP_PUT", "/user/{id}/stop");
    urls.put("USER_BLOCK_PUT", "/user/{id}/block");
    urls.put("USER_UNBLOCK_PUT", "/user/{id}/unblock");
    urls.put("USER_CATEGORIES_GET", "/user/{id}/categories");
    urls.put("USER_CATEGORIES_PUT", "/user/{id}/categories");
    urls.put("USER_PROPERTY_GET", "/user/property");
    urls.put("USER_PROPERTY_PUT", "/user/property");    
    /**USER-CHAT*/
    urls.put("USER_HISTORY_GET", "/chat");
    urls.put("USER_SEND_MESSAGE_POST", "/chat");
    urls.put("USER_SEND_NOTIFICATION_POST", "/chat/notification");
    urls.put("USER_BOT_CHAT", "/chat/bot");
    urls.put("USER_DELETE", "/chat");
    /**TICKET*/
    urls.put("TICKET_DETAILS_GET", "/ticket/{id}");
    urls.put("TICKET_GET_TICKET_HISTORY_GET", "/ticket/{user_id}/ticket_history");
    urls.put("TICKET_OPEN_PUT", "/ticket/{id}/open");
    urls.put("TICKET_CLOSE_PUT", "/ticket/{id}/close");
    urls.put("TICKET_DETAILS_PUT", "/ticket/{id}");
    urls.put("TICKET_LABEL_PUT", "/ticket/{id}/label/{label_id}");
    urls.put("TICKET_LABEL_DELETE", "/ticket/{id}/label/{label_id}");
    urls.put("TICKET_NOTE_POST", "/ticket/{id}/note");
    urls.put("TICKET_NOTE_PUT", "/ticket/{id}/note/{note_id}");
    urls.put("TICKET_NOTE_DELETE", "/ticket/{id}/note/{note_id}"); 
    urls.put("TICKET_STATISTICS_GET", "/ticket/statistics");
    /**AGENT*/
    urls.put("AGENT_AGENT_DATA_GET", "/agent/{id}");
    urls.put("AGENT_AGENT_DATA_POST", "/agent");
    urls.put("AGENT_AGENT_DATA_PUT", "/agent/{id}");
    urls.put("AGENT_AGENT_STATUS_PUT", "/agent/status");
    urls.put("AGENT_TEMPORARY_TOKEN_GET", "/agent/temporary/token");
    urls.put("AGENT_AGENT_PASSWORD_PUT", "/agent/password");
    urls.put("AGENT_AGENT_DELETE", "/agent/{id}");
    /**STATISTICS*/
    urls.put("STATISTICS_KPI_RULES_GET", "/statistics/kpi_rules");
    urls.put("STATISTICS_CHAT_GET", "/statistics/chats");
    urls.put("STATISTICS_TICKETS_GET", "/statistics/tickets");
    urls.put("STATISTICS_CUSTOM_GET", "/statistics/custom");
    urls.put("STATISTICS_AGENTS_GET", "/statistics/agents");
    
    return getMessagengerPeopleUrl() + urls.get(urlName);
    
  }

}
