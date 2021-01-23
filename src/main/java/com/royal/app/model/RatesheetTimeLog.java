package com.royal.app.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

/**
 * 
 * @author balaji
 *
 */

@Entity
@Table(name="RATESHEET_TIME_LOG")
public class RatesheetTimeLog implements Serializable 
{
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(name="AUTOGEN_RS_TIME_LOG_ID")
  private BigInteger autogenRsTimeLogId;
  
  @Column(name="AUTOGEN_RS_ID")
  private BigInteger autogenRsId;
  
  @Column(name="RS_NAME")
  private String rsName;
  
  @Column(name="PARTY_CODE")
  private String partyCode;
  
  @Column(name="PARTY_NAME")
  private String partyName;
  
  @Column(name="START_TIME")
  private Date startTime;
  
  @Column(name="END_TIME")
  private Date endTime;
  
  @Column(name="TOTAL_TIME")
  private int totalTime;
  
  @Generated(GenerationTime.INSERT)
  @Temporal(TemporalType.DATE)
  @Column(name="REC_ADD_DT")
  private Date recAddDt;
  
  @Column(name="CREATED_BY")
  private String createdBy;
  
  public RatesheetTimeLog() {}
  
  public BigInteger getAutogenRsTimeLogId() {
    return autogenRsTimeLogId;
  }

  public void setAutogenRsTimeLogId(BigInteger autogenRsTimeLogId) {
    this.autogenRsTimeLogId = autogenRsTimeLogId;
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

  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
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

  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }

  public int getTotalTime() {
    return totalTime;
  }

  public void setTotalTime(int totalTime) {
    this.totalTime = totalTime;
  }

  public Date getRecAddDt() {
    return recAddDt;
  }

  public void setRecAddDt(Date recAddDt) {
    this.recAddDt = recAddDt;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

}
