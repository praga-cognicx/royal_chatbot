package com.royal.app.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Where;

/**
 * 
 * @author balaji
 *
 */

@Entity
@Table(name="RATESHEET_MGT")
public class RatesheetMgt implements Serializable {
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(name="AUTOGEN_RS_ID")
  private BigInteger autogenRsId;
  
  @Column(name="RS_NAME")
  private String rsName;
  
  @Column(name="RS_DESC")
  private String rsDesc;
  
  @Column(name="EMAIL_IDS")
  private String emailIds;
  
  @Column(name="AGENT_CODE")
  private String agentCode;
  
  @Column(name="AGENT_NAME")
  private String agentName;
  
  @Column(name="DIVISION_ID")
  private String divisionId;
  
  @Column(name="DIVISION_NAME")
  private String divisionName;
  
  @Column(name="SEND_RATESHEET")
  private String sendRatesheet;
  
  @Column(name="SEND_UPDATES")
  private String sendUpdates;
  
  @Column(name="SHEET_PASSWORD")
  private String sheetPassword;
  
  @Column(name="STATUS")
  private String status;
  
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
  
  @Column(nullable=true,name="PRIORITY")
  private Integer priority;
  
  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "AUTOGEN_RS_ID", nullable = true ,insertable = false,updatable = false)
  @Where(clause = "status = 'Active'")
  @OrderBy(clause = "SECTOR_NAME ASC, CATEGORY_NAME DESC, PARTY_NAME ASC")
  private List<RatesheetMapping> ratesheetMapping = new ArrayList<RatesheetMapping>();
  
  public RatesheetMgt() {}

  
  public Integer getPriority() {
    return priority;
  }


  public void setPriority(Integer priority) {
    this.priority = priority;
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

  public String getRsDesc() {
    return rsDesc;
  }

  public void setRsDesc(String rsDesc) {
    this.rsDesc = rsDesc;
  }

  public String getEmailIds() {
    return emailIds;
  }

  public void setEmailIds(String emailIds) {
    this.emailIds = emailIds;
  }

  public String getAgentCode() {
    return agentCode;
  }

  public void setAgentCode(String agentCode) {
    this.agentCode = agentCode;
  }

  public String getAgentName() {
    return agentName;
  }

  public void setAgentName(String agentName) {
    this.agentName = agentName;
  }

  public String getDivisionId() {
    return divisionId;
  }

  public void setDivisionId(String divisionId) {
    this.divisionId = divisionId;
  }

  public String getDivisionName() {
    return divisionName;
  }

  public void setDivisionName(String divisionName) {
    this.divisionName = divisionName;
  }

  public String getSendRatesheet() {
    return sendRatesheet;
  }

  public void setSendRatesheet(String sendRatesheet) {
    this.sendRatesheet = sendRatesheet;
  }

  public String getSendUpdates() {
    return sendUpdates;
  }

  public void setSendUpdates(String sendUpdates) {
    this.sendUpdates = sendUpdates;
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
  
  public String getSheetPassword() {
    return sheetPassword;
  }

  public void setSheetPassword(String sheetPassword) {
    this.sheetPassword = sheetPassword;
  }

  public List<RatesheetMapping> getRatesheetMapping() {
    return ratesheetMapping;
  }
  
  
}
