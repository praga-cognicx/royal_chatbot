package com.royal.app.message.response;

import java.math.BigInteger;

public class RatesheetMapResponse {
  
  private boolean mapped;
  private BigInteger id;
  private String partyCode;
  private String partyName;
  private String displayName;
  private String categoryCode;
  private String categoryName;
  private String sectorCode;
  private String sectorName;
  private String cityCode;
  private String cityName;
  
  
  public boolean isMapped() {
    return mapped;
  }
  public void setMapped(boolean mapped) {
    this.mapped = mapped;
  }
  public BigInteger getId() {
    return id;
  }
  public void setId(BigInteger id) {
    this.id = id;
  }
  public String getPartyCode() {
    return partyCode;
  }
  public void setPartyCode(String partyCode) {
    this.partyCode = partyCode;
  }
  public String getPartyName() {
    return partyName;
  }
  public void setPartyName(String partyName) {
    this.partyName = partyName;
  }
  public String getDisplayName() {
    return displayName;
  }
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }
  public String getCategoryCode() {
    return categoryCode;
  }
  public void setCategoryCode(String categoryCode) {
    this.categoryCode = categoryCode;
  }
  public String getCategoryName() {
    return categoryName;
  }
  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }
  public String getSectorCode() {
    return sectorCode;
  }
  public void setSectorCode(String sectorCode) {
    this.sectorCode = sectorCode;
  }
  public String getSectorName() {
    return sectorName;
  }
  public void setSectorName(String sectorName) {
    this.sectorName = sectorName;
  }
  public String getCityCode() {
    return cityCode;
  }
  public void setCityCode(String cityCode) {
    this.cityCode = cityCode;
  }
  public String getCityName() {
    return cityName;
  }
  public void setCityName(String cityName) {
    this.cityName = cityName;
  }
  
}
