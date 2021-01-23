package com.royal.app.message.request;

import java.math.BigInteger;

public class RatesheetUpdateViewRequest {
  
  private String updateName;
  private String partyCode;
  private String from;
  private String to;
  private BigInteger rsId;
  private String createdBy;
  
  public String getUpdateName() {
    return updateName;
  }
  public void setUpdateName(String updateName) {
    this.updateName = updateName;
  }
  public String getPartyCode() {
    return partyCode;
  }
  public void setPartyCode(String partyCode) {
    this.partyCode = partyCode;
  }
  public String getFrom() {
    return from;
  }
  public void setFrom(String from) {
    this.from = from;
  }
  public String getTo() {
    return to;
  }
  public void setTo(String to) {
    this.to = to;
  }
  public BigInteger getRsId() {
    return rsId;
  }
  public void setRsId(BigInteger rsId) {
    this.rsId = rsId;
  }
  public String getCreatedBy() {
    return createdBy;
  }
  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }
  
}
