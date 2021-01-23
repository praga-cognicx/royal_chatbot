package com.royal.app.message.request;

import java.math.BigInteger;
import java.util.Date;
import javax.validation.constraints.NotEmpty;

public class RSUpdatesCreateReq {
  
  private BigInteger rsUpdatesId;
  @NotEmpty
  private String updateName;
  @NotEmpty
  private String partyCode;
  private String partyName;
  private String updateDetails;
  @NotEmpty
  private String validFrom;
  private String validTill;
  private String internalNote;
  private BigInteger rsId;
  private String rsName;
  private String status;
  private String updatedBy;
  private String createdBy;
  
  public BigInteger getRsUpdatesId() {
    return rsUpdatesId;
  }
  public void setRsUpdatesId(BigInteger rsUpdatesId) {
    this.rsUpdatesId = rsUpdatesId;
  }
  public String getUpdateName() {
    return updateName;
  }
  public void setUpdateName(String updateName) {
    this.updateName = updateName;
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
  public String getUpdateDetails() {
    return updateDetails;
  }
  public void setUpdateDetails(String updateDetails) {
    this.updateDetails = updateDetails;
  }
  public String getValidFrom() {
    return validFrom;
  }
  public void setValidFrom(String validFrom) {
    this.validFrom = validFrom;
  }
  public String getValidTill() {
    return validTill;
  }
  public void setValidTill(String validTill) {
    this.validTill = validTill;
  }
  public String getInternalNote() {
    return internalNote;
  }
  public void setInternalNote(String internalNote) {
    this.internalNote = internalNote;
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
  public String getUpdatedBy() {
    return updatedBy;
  }
  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }
  public String getCreatedBy() {
    return createdBy;
  }
  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }
}
