package com.royal.app.message.request;

import javax.validation.constraints.NotEmpty;

public class RateSheetMgtRequest {

  @NotEmpty
  private String rsName;
  private String rsDesc;
  @NotEmpty
  private String emailIds;
  @NotEmpty
  private String agentCode;
  @NotEmpty
  private String agentName;
  @NotEmpty
  private String divisionId;
  @NotEmpty
  private String divisionName;
  @NotEmpty
  private String sendRatesheet;
  @NotEmpty
  private String sendUpdates;
  @NotEmpty
  private String sheetPassword;
  
  public String getRsName() {
    return rsName;
  }

  public void setRsName(String rsName) {
    this.rsName = rsName;
  }

  public String getRsDesc() {
    return rsDesc;
  }

  public void setRsDesc(String rsDesc) {
    this.rsDesc = rsDesc;
  }

  public String getEmailIds() {
    return emailIds;
  }

  public void setEmailIds(String emailIds) {
    this.emailIds = emailIds;
  }

  public String getAgentCode() {
    return agentCode;
  }

  public void setAgentCode(String agentCode) {
    this.agentCode = agentCode;
  }

  public String getAgentName() {
    return agentName;
  }

  public void setAgentName(String agentName) {
    this.agentName = agentName;
  }

  public String getDivisionId() {
    return divisionId;
  }

  public void setDivisionId(String divisionId) {
    this.divisionId = divisionId;
  }

  public String getDivisionName() {
    return divisionName;
  }

  public void setDivisionName(String divisionName) {
    this.divisionName = divisionName;
  }

  public String getSendRatesheet() {
    return sendRatesheet;
  }

  public void setSendRatesheet(String sendRatesheet) {
    this.sendRatesheet = sendRatesheet;
  }

  public String getSendUpdates() {
    return sendUpdates;
  }

  public void setSendUpdates(String sendUpdates) {
    this.sendUpdates = sendUpdates;
  }

  public String getSheetPassword() {
    return sheetPassword;
  }

  public void setSheetPassword(String sheetPassword) {
    this.sheetPassword = sheetPassword;
  }

}
