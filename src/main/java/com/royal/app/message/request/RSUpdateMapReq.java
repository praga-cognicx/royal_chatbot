package com.royal.app.message.request;

import java.math.BigInteger;

public class RSUpdateMapReq {
  
  private BigInteger mapId;
  private BigInteger rsUpdatesId;
  private BigInteger rsId;
  private String rsName;
  private String status;
  
  public BigInteger getMapId() {
    return mapId;
  }
  public void setMapId(BigInteger mapId) {
    this.mapId = mapId;
  }
  public BigInteger getRsUpdatesId() {
    return rsUpdatesId;
  }
  public void setRsUpdatesId(BigInteger rsUpdatesId) {
    this.rsUpdatesId = rsUpdatesId;
  }
  public BigInteger getRsId() {
    return rsId;
  }
  public void setRsId(BigInteger rsId) {
    this.rsId = rsId;
  }
  public String getRsName() {
    return rsName;
  }
  public void setRsName(String rsName) {
    this.rsName = rsName;
  }
  public String getStatus() {
    return status;
  }
  public void setStatus(String status) {
    this.status = status;
  }

}
