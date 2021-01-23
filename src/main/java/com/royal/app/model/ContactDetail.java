package com.royal.app.model;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import java.sql.Timestamp;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the contact_details database table.
 * 
 */
@Entity
@Table(name="contact_details")
@NamedQuery(name="ContactDetail.findAll", query="SELECT c FROM ContactDetail c")
public class ContactDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="AUTOGEN_CONTACT_DETAILS_ID")
	private BigInteger autogenContactDetailsId;
	
	@Column(name="AUTOGEN_CLIENT_DETAILS_ID")
	private BigInteger autogenClientDetailsId;

	@Column(name="CREATED_BY")
	private String createdBy;

	private String email;

	@Column(name="MOBILE_NUMBER")
	private String mobileNumber;

	@Column(name="NOTIFICATION_STATUS")
	private String notificationStatus;

	@Column(name="PERSON_NAME")
	private String personName;

	@Generated(GenerationTime.INSERT)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REC_ADD_DT")
	private Date recAddDt;

	@Generated(GenerationTime.ALWAYS)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REC_UPDATE_DT")
	private Date recUpdateDt;

	@Column(name="UPDATED_BY")
	private String updatedBy;
	
	private String status;

	
	public ContactDetail() {
	}

	public BigInteger getAutogenContactDetailsId() {
		return this.autogenContactDetailsId;
	}

	public void setAutogenContactDetailsId(BigInteger autogenContactDetailsId) {
		this.autogenContactDetailsId = autogenContactDetailsId;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobileNumber() {
		return this.mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getNotificationStatus() {
		return this.notificationStatus;
	}

	public void setNotificationStatus(String notificationStatus) {
		this.notificationStatus = notificationStatus;
	}

	public String getPersonName() {
		return this.personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public Date getRecAddDt() {
		return this.recAddDt;
	}

	public void setRecAddDt(Timestamp recAddDt) {
		this.recAddDt = recAddDt;
	}

	public Date getRecUpdateDt() {
		return this.recUpdateDt;
	}

	public void setRecUpdateDt(Timestamp recUpdateDt) {
		this.recUpdateDt = recUpdateDt;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	public BigInteger getAutogenClientDetailsId() {
		return autogenClientDetailsId;
	}

	public void setAutogenClientDetailsId(BigInteger autogenClientDetailsId) {
		this.autogenClientDetailsId = autogenClientDetailsId;
	}
	
	
	
	

}