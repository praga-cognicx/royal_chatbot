package com.royal.app.shared.dto;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import com.royal.app.model.RatesheetUpdatesMap;

public class RateSheetUpdatesDto {
  
  private BigInteger autogenRsUpdatesId;
  private String updateName;
  private String partyCode;
  private String partyName;
  private String updateDetails;
  private Date validFrom;
  private Date validTill;
  private String internalNote;
  private String status;
  private Date recAddDt;
  private Date recUpdateDt;
  private String updatedBy;
  private String createdBy;
  List<RatesheetUpdatesMap> RatesheetUpdatesMaps;
  private Object[] objectArr;
  private Object object;
  public BigInteger getAutogenRsUpdatesId() {
    return autogenRsUpdatesId;
  }
  public void setAutogenRsUpdatesId(BigInteger autogenRsUpdatesId) {
    this.autogenRsUpdatesId = autogenRsUpdatesId;
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
  public Date getValidFrom() {
    return validFrom;
  }
  public void setValidFrom(Date validFrom) {
    this.validFrom = validFrom;
  }
  public Date getValidTill() {
    return validTill;
  }
  public void setValidTill(Date validTill) {
    this.validTill = validTill;
  }
  public String getInternalNote() {
    return internalNote;
  }
  public void setInternalNote(String internalNote) {
    this.internalNote = internalNote;
  }
  public String getStatus() {
    return status;
  }
  public void setStatus(String status) {
    this.status = status;
  }
  public Date getRecAddDt() {
    return recAddDt;
  }
  public void setRecAddDt(Date recAddDt) {
    this.recAddDt = recAddDt;
  }
  public Date getRecUpdateDt() {
    return recUpdateDt;
  }
  public void setRecUpdateDt(Date recUpdateDt) {
    this.recUpdateDt = recUpdateDt;
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
  public List<RatesheetUpdatesMap> getRatesheetUpdatesMaps() {
    return RatesheetUpdatesMaps;
  }
  public void setRatesheetUpdatesMaps(List<RatesheetUpdatesMap> ratesheetUpdatesMaps) {
    RatesheetUpdatesMaps = ratesheetUpdatesMaps;
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

}
