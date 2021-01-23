package com.royal.app.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author balaji
 *
 */

@Entity
@Table(name="RATESHEET_AGENT_MAP")
public class RatesheetAgentMap implements Serializable {
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(name="AutoMapId")
  private int autoMapId;
  
  @Column(name="AutoGen_RS_ID")
  private int autogenRsId;
  
  @Column(name="RS_Name")
  private String rsName;
  
  @Column(name="AgentCode")
  private String agentCode;
  
  @Column(name="Ratesheet_EmailTo")
  private String ratesheetEmailTo;
  
  @Column(name="Ratesheet_EmailCC")
  private String ratesheetEmailCC;
  
  @Column(name="EmailActive")
  private int emailActive;
  
  @Column(name="CreatedDate")
  private Date createdDate;

  @Column(name="UpdatedDate")
  private Date updatedDate;
  
  @Column(name="UpdatedBy")
  private String updatedBy;
  
  @Column(name="CreatedBy")
  private String createdBy;

  
  public int getAutoMapId() {
    return autoMapId;
  }

  public void setAutoMapId(int autoMapId) {
    this.autoMapId = autoMapId;
  }

  public int getAutogenRsId() {
    return autogenRsId;
  }

  public void setAutogenRsId(int autogenRsId) {
    this.autogenRsId = autogenRsId;
  }

  public String getRsName() {
    return rsName;
  }

  public void setRsName(String rsName) {
    this.rsName = rsName;
  }

  public String getAgentCode() {
    return agentCode;
  }

  public void setAgentCode(String agentCode) {
    this.agentCode = agentCode;
  }

  public String getRatesheetEmailTo() {
    return ratesheetEmailTo;
  }

  public void setRatesheetEmailTo(String ratesheetEmailTo) {
    this.ratesheetEmailTo = ratesheetEmailTo;
  }

  public String getRatesheetEmailCC() {
    return ratesheetEmailCC;
  }

  public void setRatesheetEmailCC(String ratesheetEmailCC) {
    this.ratesheetEmailCC = ratesheetEmailCC;
  }

  public int getEmailActive() {
    return emailActive;
  }

  public void setEmailActive(int emailActive) {
    this.emailActive = emailActive;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public Date getUpdatedDate() {
    return updatedDate;
  }

  public void setUpdatedDate(Date updatedDate) {
    this.updatedDate = updatedDate;
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
