package com.royal.app.message.response;

import java.util.List;

public class RSUpdatesDropdowns {
  
  private List<KeyValuePair> parties;
  private List<KeyValuePair> updateTypes;
  private List<KeyValuePair> users;
  private List<KeyValuePair> ratesheets;
  
  public List<KeyValuePair> getParties() {
    return parties;
  }
  public void setParties(List<KeyValuePair> parties) {
    this.parties = parties;
  }
  public List<KeyValuePair> getUpdateTypes() {
    return updateTypes;
  }
  public void setUpdateTypes(List<KeyValuePair> updateTypes) {
    this.updateTypes = updateTypes;
  }
  public List<KeyValuePair> getUsers() {
    return users;
  }
  public void setUsers(List<KeyValuePair> users) {
    this.users = users;
  }
  public List<KeyValuePair> getRatesheets() {
    return ratesheets;
  }
  public void setRatesheets(List<KeyValuePair> ratesheets) {
    this.ratesheets = ratesheets;
  }
}
