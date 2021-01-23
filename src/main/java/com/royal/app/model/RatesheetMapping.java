package com.royal.app.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.Where;

/**
 * 
 * @author balaji
 *
 */

@Entity
@Table(name="RATESHEET_MAPPING")
public class RatesheetMapping implements Serializable{
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(name="AUTOGEN_RS_MAP_ID")
  private BigInteger autogenRsMapId;
  
  @Column(name="AUTOGEN_RS_ID")
  private BigInteger autogenRsId;
  
  @Column(name="SECTOR_CODE")
  private String sectorCode;
  
  @Column(name="SECTOR_NAME")
  private String sectorName;
  
  @Column(name="CATEGORY_CODE")
  private String categoryCode;
  
  @Column(name="CATEGORY_NAME")
  private String categoryName;
  
  @Column(name="COUNTRY_CODE")
  private String countryCode;
  
  @Column(name="COUNTRY_NAME")
  private String countryName;
  
  @Column(name="CITY_CODE")
  private String cityCode;
  
  @Column(name="CITY_NAME")
  private String cityName;
  
  @Column(name="PARTY_CODE")
  private String partyCode;
  
  @Column(name="PARTY_NAME")
  private String partyName;
  
/*  @Column(name="FROM_DATE")
  private Date fromDate;
  
  @Column(name="TO_DATE")
  private Date toDate;*/
  
  @Column(name="STATUS")
  private String status;
  
  @Column(name="DISPLAY_NAME")
  private String displayName;
  
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
  
  @Column(name="CURRENT_UPDATE")
  private String currentUpdate;
  
  
  public String getCurrentUpdate() {
    return currentUpdate;
  }
  public void setCurrentUpdate(String currentUpdate) {
    this.currentUpdate = currentUpdate;
  }
  
  public RatesheetMapping() {}

  public BigInteger getAutogenRsMapId() {
    return autogenRsMapId;
  }

  public void setAutogenRsMapId(BigInteger autogenRsMapId) {
    this.autogenRsMapId = autogenRsMapId;
  }

  public BigInteger getAutogenRsId() {
    return autogenRsId;
  }

  public void setAutogenRsId(BigInteger autogenRsId) {
    this.autogenRsId = autogenRsId;
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
  
/*  public Date getFromDate() {
    return fromDate;
  }

  public void setFromDate(Date fromDate) {
    this.fromDate = fromDate;
  }

  public Date getToDate() {
    return toDate;
  }

  public void setToDate(Date toDate) {
    this.toDate = toDate;
  }*/

  public String getCountryCode() {
    return countryCode;
  }

  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }

  public String getCountryName() {
    return countryName;
  }

  public void setCountryName(String countryName) {
    this.countryName = countryName;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }
  
}
