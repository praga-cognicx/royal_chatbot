package com.royal.app.message.request;

import java.math.BigInteger;
import com.royal.app.shared.dto.RatesheetMgtDto;

public class RateSheetMgtPriorityReqRes {
  
  private BigInteger id;
  private String rsName;
  private String rsDesc;
  private int priority;
  
public RateSheetMgtPriorityReqRes() {}
  
  public RateSheetMgtPriorityReqRes(RatesheetMgtDto ratesheetMgtDto) {
    id = ratesheetMgtDto.getAutogenRsId();
    rsName = ratesheetMgtDto.getRsName();
    rsDesc = ratesheetMgtDto.getRsDesc();
    priority = ratesheetMgtDto.getPriority();
  }

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

  public int getPriority() {
    return priority;
  }

  public void setPriority(int priority) {
    this.priority = priority;
  }
  
  

}
