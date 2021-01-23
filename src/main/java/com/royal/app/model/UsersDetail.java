package com.royal.app.model;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import java.util.Date;
import java.math.BigInteger;


/**
 * The persistent class for the users_details database table.
 * 
 */
@Entity
@Table(name="users_details")
@NamedQuery(name="UsersDetail.findAll", query="SELECT u FROM UsersDetail u")
public class UsersDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="AUTOGEN_USERS_DETAILS_ID")
	private BigInteger autogenUsersDetailsId;

	@Column(name="AUTOGEN_USERS_ID")
	private BigInteger autogenUsersId;
	
	@Column(name="AUTOGEN_ROLES_ID")
	private BigInteger autogenRolesId;
	
	@Column(name="ROLES_NAME")
	private String rolesName;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Generated(GenerationTime.INSERT)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REC_ADD_DT")
	private Date recAddDt;

	@Generated(GenerationTime.ALWAYS)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REC_UPDATE_DT")
	private Date recUpdateDt;

	@Column(name="SUPERVISOR_USERS_ID")
	private String supervisorUsersId;
	
	@Column(name="SUPERVISOR_USERS_NAME")
	private String supervisorUsersName;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	public UsersDetail() {
	}

	public BigInteger getAutogenUsersDetailsId() {
		return this.autogenUsersDetailsId;
	}

	public void setAutogenUsersDetailsId(BigInteger autogenUsersDetailsId) {
		this.autogenUsersDetailsId = autogenUsersDetailsId;
	}

	public BigInteger getAutogenUsersId() {
		return this.autogenUsersId;
	}

	public void setAutogenUsersId(BigInteger autogenUsersId) {
		this.autogenUsersId = autogenUsersId;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}


	public Date getRecAddDt() {
		return this.recAddDt;
	}

	public void setRecAddDt(Date recAddDt) {
		this.recAddDt = recAddDt;
	}

	public Date getRecUpdateDt() {
		return this.recUpdateDt;
	}

	public void setRecUpdateDt(Date recUpdateDt) {
		this.recUpdateDt = recUpdateDt;
	}

	public String getSupervisorUsersId() {
		return this.supervisorUsersId;
	}

	public void setSupervisorUsersId(String supervisorUsersId) {
		this.supervisorUsersId = supervisorUsersId;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public BigInteger getAutogenRolesId() {
		return autogenRolesId;
	}

	public void setAutogenRolesId(BigInteger autogenRolesId) {
		this.autogenRolesId = autogenRolesId;
	}

	public String getRolesName() {
		return rolesName;
	}

	public void setRolesName(String rolesName) {
		this.rolesName = rolesName;
	}

	public String getSupervisorUsersName() {
		return supervisorUsersName;
	}

	public void setSupervisorUsersName(String supervisorUsersName) {
		this.supervisorUsersName = supervisorUsersName;
	}

	
}