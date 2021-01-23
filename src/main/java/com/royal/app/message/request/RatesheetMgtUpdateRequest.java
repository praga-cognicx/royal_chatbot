package com.royal.app.message.request;

import java.math.BigInteger;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class RatesheetMgtUpdateRequest {
  
  @NotNull
  private BigInteger id;
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
  private String status;
  
  public BigInteger getId() {
    return id;
  }

  public void setId(BigInteger id) {
    this.id = id;
  }

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

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getSheetPassword() {
    return sheetPassword;
  }

  public void setSheetPassword(String sheetPassword) {
    this.sheetPassword = sheetPassword;
  }
}
