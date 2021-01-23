package com.royal.app.message.request;

import java.math.BigInteger;

public class RatesheetMapsSearchRequest {
  
  private BigInteger ratesheetId;
  private String categoryCode;
  private String sectorCode;
  private String cityCode;
  
  public BigInteger getRatesheetId() {
    return ratesheetId;
  }
  public void setRatesheetId(BigInteger ratesheetId) {
    this.ratesheetId = ratesheetId;
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
