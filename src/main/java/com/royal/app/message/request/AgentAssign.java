package com.royal.app.message.request;

public class AgentAssign {
  private String agent_id;
  private String[] batch_ids;
  private String apikey;
  
  public String getAgent_id() {
    return agent_id;
  }
  public void setAgent_id(String agent_id) {
    this.agent_id = agent_id;
  }
  public String[] getBatch_ids() {
    return batch_ids;
  }
  public void setBatch_ids(String[] batch_ids) {
    this.batch_ids = batch_ids;
  }
  public String getApikey() {
    return apikey;
  }
  public void setApikey(String apikey) {
    this.apikey = apikey;
  }
  
  
  
}
