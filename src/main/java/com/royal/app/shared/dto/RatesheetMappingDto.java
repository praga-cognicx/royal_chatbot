package com.royal.app.shared.dto;

import java.math.BigInteger;
import java.util.List;
import com.royal.app.message.request.RatesheetMappingRequest;

public class RatesheetMappingDto {
  
  private BigInteger autogenRsId;
  private List<RatesheetMappingRequest> ratesheetMappingList;
  private Object resultObj;
  private boolean status;
  
  public BigInteger getAutogenRsId() {
    return autogenRsId;
  }
  public void setAutogenRsId(BigInteger autogenRsId) {
    this.autogenRsId = autogenRsId;
  }
  public List<RatesheetMappingRequest> getRatesheetMappingList() {
    return ratesheetMappingList;
  }
  public void setRatesheetMappingList(List<RatesheetMappingRequest> ratesheetMappingList) {
    this.ratesheetMappingList = ratesheetMappingList;
  }
  public Object getResultObj() {
    return resultObj;
  }
  public void setResultObj(Object resultObj) {
    this.resultObj = resultObj;
  }
  public boolean isStatus() {
    return status;
  }
  public void setStatus(boolean status) {
    this.status = status;
  }
}
