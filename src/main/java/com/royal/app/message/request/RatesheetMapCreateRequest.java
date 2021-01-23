package com.royal.app.message.request;

import java.math.BigInteger;
import java.util.List;
import javax.validation.constraints.NotNull;

public class RatesheetMapCreateRequest {
  
  @NotNull
  private BigInteger ratesheetId;
  private List<RatesheetMappingRequest> ratesheetMappingList;
  
  public BigInteger getRatesheetId() {
    return ratesheetId;
  }
  public void setRatesheetId(BigInteger ratesheetId) {
    this.ratesheetId = ratesheetId;
  }
  public List<RatesheetMappingRequest> getRatesheetMappingList() {
    return ratesheetMappingList;
  }
  public void setRatesheetMappingList(List<RatesheetMappingRequest> ratesheetMappingList) {
    this.ratesheetMappingList = ratesheetMappingList;
  }
}
