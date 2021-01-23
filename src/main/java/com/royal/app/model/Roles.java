package com.royal.app.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the roles database table.
 * 
 */
@Entity
@Table(name="roles")
@NamedQuery(name="Roles.findAll", query="SELECT r FROM Roles r")
public class Roles implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="AUTOGEN_ROLES_ID")
	private String autogenRolesId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REC_ADD_DT")
	private Date recAddDt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="REC_UPDATE_DT")
	private Date recUpdateDt;

	@Column(name="ROLES_NAME")
	private String rolesName;

	private String status;

	public Roles() {
	}

	public String getAutogenRolesId() {
		return this.autogenRolesId;
	}

	public void setAutogenRolesId(String autogenRolesId) {
		this.autogenRolesId = autogenRolesId;
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

	public String getRolesName() {
		return this.rolesName;
	}

	public void setRolesName(String rolesName) {
		this.rolesName = rolesName;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}