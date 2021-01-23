package com.royal.app.message.request;

import lombok.Data;

@Data
public class SendMessage {
  private String apikey;
  private String id;
  private String message;
  private String attachement;
  private String filename;
  private String conversation_status;
  private String buttons;
  private String meta;
  private String batch_ids;
  private String user_filter;
  public String getApikey() {
    return apikey;
  }
  public void setApikey(String apikey) {
    this.apikey = apikey;
  }
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public String getMessage() {
    return message;
  }
  public void setMessage(String message) {
    this.message = message;
  }
  public String getAttachement() {
    return attachement;
  }
  public void setAttachement(String attachement) {
    this.attachement = attachement;
  }
  public String getFilename() {
    return filename;
  }
  public void setFilename(String filename) {
    this.filename = filename;
  }
  public String getConversation_status() {
    return conversation_status;
  }
  public void setConversation_status(String conversation_status) {
    this.conversation_status = conversation_status;
  }
  public String getButtons() {
    return buttons;
  }
  public void setButtons(String buttons) {
    this.buttons = buttons;
  }
  public String getMeta() {
    return meta;
  }
  public void setMeta(String meta) {
    this.meta = meta;
  }
  public String getBatch_ids() {
    return batch_ids;
  }
  public void setBatch_ids(String batch_ids) {
    this.batch_ids = batch_ids;
  }
  public String getUser_filter() {
    return user_filter;
  }
  public void setUser_filter(String user_filter) {
    this.user_filter = user_filter;
  }
     
}
