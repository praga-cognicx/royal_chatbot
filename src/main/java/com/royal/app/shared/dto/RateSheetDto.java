package com.royal.app.shared.dto;

import java.math.BigInteger;

public class RateSheetDto {
  
  private String countryCode;
  private String partyCode;
  private String agentCode;
  private String fromDate;
  private String toDate;
  private Object[] objectArr;
  private Object object;
  private BigInteger autogenRsId;
  private String partyName;
  private String createdBy;
  private String rsName;
  private String displayName;
  private String sheetPassword;
  private String currentUpdate;
  
  
  public String getCurrentUpdate() {
    return currentUpdate;
  }
  public void setCurrentUpdate(String currentUpdate) {
    this.currentUpdate = currentUpdate;
  }
  public String getRsName() {
    return rsName;
  }
  public void setRsName(String rsName) {
    this.rsName = rsName;
  }
  public String getCreatedBy() {
    return createdBy;
  }
  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }
  public String getPartyName() {
    return partyName;
  }
  public void setPartyName(String partyName) {
    this.partyName = partyName;
  }
  public BigInteger getAutogenRsId() {
    return autogenRsId;
  }
  public void setAutogenRsId(BigInteger autogenRsId) {
    this.autogenRsId = autogenRsId;
  }
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
  public Object[] getObjectArr() {
    return objectArr;
  }
  public void setObjectArr(Object[] objectArr) {
    this.objectArr = objectArr;
  }
  public Object getObject() {
    return object;
  }
  public void setObject(Object object) {
    this.object = object;
  }
  public String getDisplayName() {
    return displayName;
  }
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }
  public String getSheetPassword() {
    return sheetPassword;
  }
  public void setSheetPassword(String sheetPassword) {
    this.sheetPassword = sheetPassword;
  }
    
}
