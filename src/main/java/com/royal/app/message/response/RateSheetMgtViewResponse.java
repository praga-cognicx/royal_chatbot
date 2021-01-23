package com.royal.app.message.response;

import java.math.BigInteger;
import java.util.List;
import com.royal.app.shared.dto.RatesheetMgtDto;

public class RateSheetMgtViewResponse {
  
  private BigInteger id;
  private String rsName;
  private String rsDesc;
  private String emailIds;
  private String agentCode;
  private String agentName;
  private String divisionId;
  private String divisionName;
  private String sendRatesheet;
  private String sendUpdates;
  private String sheetPassword;
  private String status;
  private List<RatesheetMapResponse> ratesheetMaps;
  
  public RateSheetMgtViewResponse() {}
  
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
  public String getSheetPassword() {
    return sheetPassword;
  }
  public void setSheetPassword(String sheetPassword) {
    this.sheetPassword = sheetPassword;
  }
  public String getStatus() {
    return status;
  }
  public void setStatus(String status) {
    this.status = status;
  }

  public List<RatesheetMapResponse> getRatesheetMaps() {
    return ratesheetMaps;
  }

  public void setRatesheetMaps(List<RatesheetMapResponse> ratesheetMaps) {
    this.ratesheetMaps = ratesheetMaps;
  }
  
}
