package com.royal.app.shared.dto;

import java.math.BigInteger;

public class RatesheetMgtDto {
  
  private BigInteger autogenRsId;
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
  private Object result;
  private Object[] resultArr;
  private boolean flag;
  private String categoryCode;
  private String sectorCode;
  private String cityCode;
  private String errorMsg;
  private int priority;
  
  
  public int getPriority() {
    return priority;
  }
  public void setPriority(int priority) {
    this.priority = priority;
  }
  public String getErrorMsg() {
    return errorMsg;
  }
  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
  }
  public BigInteger getAutogenRsId() {
    return autogenRsId;
  }
  public void setAutogenRsId(BigInteger autogenRsId) {
    this.autogenRsId = autogenRsId;
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
  public Object getResult() {
    return result;
  }
  public void setResult(Object result) {
    this.result = result;
  }
  public Object[] getResultArr() {
    return resultArr;
  }
  public void setResultArr(Object[] resultArr) {
    this.resultArr = resultArr;
  }
  public String getStatus() {
    return status;
  }
  public void setStatus(String status) {
    this.status = status;
  }
  public boolean isFlag() {
    return flag;
  }
  public void setFlag(boolean flag) {
    this.flag = flag;
  }
  public String getCategoryCode() {
    return categoryCode;
  }
  public void setCategoryCode(String categoryCode) {
    this.categoryCode = categoryCode;
  }
  public String getSectorCode() {
    return sectorCode;
  }
  public void setSectorCode(String sectorCode) {
    this.sectorCode = sectorCode;
  }
  public String getCityCode() {
    return cityCode;
  }
  public void setCityCode(String cityCode) {
    this.cityCode = cityCode;
  }

}
