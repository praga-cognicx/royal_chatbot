package com.royal.app.message.request;

public class RateSheetRequest {

  private String countryCode;
  private String partyCode;
  private String agentCode;
  private String fromDate;
  private String toDate;
  
  public String getCountryCode() {
    return countryCode;
  }
  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }
  public String getPartyCode() {
    return partyCode;
  }
  public void setPartyCode(String partyCode) {
    this.partyCode = partyCode;
  }
  public String getAgentCode() {
    return agentCode;
  }
  public void setAgentCode(String agentCode) {
    this.agentCode = agentCode;
  }
  public String getFromDate() {
    return fromDate;
  }
  public void setFromDate(String fromDate) {
    this.fromDate = fromDate;
  }
  public String getToDate() {
    return toDate;
  }
  public void setToDate(String toDate) {
    this.toDate = toDate;
  }
 
}
