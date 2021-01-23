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
@Table(name="RATESHEET_UPDATES_MAP")
public class RatesheetUpdatesMap implements Serializable {
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(name="AUTOGEN_RS_UPDATES_MAP_ID")
  private BigInteger autogenRsUpdatesMapId;
  
  @Column(name="AUTOGEN_RS_UPDATES_ID")
  private BigInteger autogenRsUpdatesId;
  
  @Column(name="AUTOGEN_RS_ID")
  private BigInteger autogenRsId;
  
  @Column(name="RS_NAME")
  private String rsName;
  
  @Column(name="STATUS")
  private String status;
  /*
  @Generated(GenerationTime.INSERT)
  @Temporal(TemporalType.DATE)
  @Column(name="REC_ADD_DT")
  private Date recAddDt;

  @Generated(GenerationTime.ALWAYS)
  @Temporal(TemporalType.DATE)
  @Column(name="REC_UPDATE_DT")
  private Date recUpdateDt;
  
  @Column(name="UPDATED_BY")
  private String updatedBy;
  
  @Column(name="CREATED_BY")
  private String createdBy;
*/
  public RatesheetUpdatesMap() {}

  public BigInteger getAutogenRsUpdatesMapId() {
    return autogenRsUpdatesMapId;
  }

  public void setAutogenRsUpdatesMapId(BigInteger autogenRsUpdatesMapId) {
    this.autogenRsUpdatesMapId = autogenRsUpdatesMapId;
  }

  public BigInteger getAutogenRsUpdatesId() {
    return autogenRsUpdatesId;
  }

  public void setAutogenRsUpdatesId(BigInteger autogenRsUpdatesId) {
    this.autogenRsUpdatesId = autogenRsUpdatesId;
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

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
  /*
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
  }*/
  
}
