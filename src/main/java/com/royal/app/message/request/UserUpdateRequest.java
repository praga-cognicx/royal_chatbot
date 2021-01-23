package com.royal.app.message.request;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import com.royal.app.model.Reports;
import com.royal.app.shared.dto.UserLeaveDetailsDto;

public class UserUpdateRequest {

	@NotNull
	private BigInteger id;
	private BigInteger inventoryCategoryId;
	private String inventoryCategoryName;
	private String email;
	private String firstName;
	private String lastName;
	private String mobileNumber;
	private String status;
	private List<Reports> reports = new ArrayList<>();
	private List<UserRegionRequest> userInventoryMaps = new ArrayList<>();
	private List<UserLeaveDetailsDto> userLeaveDetails = new ArrayList<>();
	private String supervisorUsersId;
	private String supervisorUsersName;
	private BigInteger roleId;

	@NotBlank
	private String rolesName;

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public BigInteger getInventoryCategoryId() {
		return inventoryCategoryId;
	}

	public void setInventoryCategoryId(BigInteger inventoryCategoryId) {
		this.inventoryCategoryId = inventoryCategoryId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getSupervisorUsersId() {
		return supervisorUsersId;
	}

	public void setSupervisorUsersId(String supervisorUsersId) {
		this.supervisorUsersId = supervisorUsersId;
	}

	public BigInteger getRoleId() {
		return roleId;
	}

	public void setRoleId(BigInteger roleId) {
		this.roleId = roleId;
	}

	public String getInventoryCategoryName() {
		return inventoryCategoryName;
	}

	public void setInventoryCategoryName(String inventoryCategoryName) {
		this.inventoryCategoryName = inventoryCategoryName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Reports> getReports() {
		return reports;
	}

	public void setReports(List<Reports> reports) {
		this.reports = reports;
	}

	public String getSupervisorUsersName() {
		return supervisorUsersName;
	}

	public void setSupervisorUsersName(String supervisorUsersName) {
		this.supervisorUsersName = supervisorUsersName;
	}

	public String getRolesName() {
		return rolesName;
	}

	public void setRolesName(String roleName) {
		this.rolesName = roleName;
	}

	public List<UserRegionRequest> getUserInventoryMaps() {
		return userInventoryMaps;
	}

	public void setUserInventoryMaps(List<UserRegionRequest> userInventoryMaps) {
		this.userInventoryMaps = userInventoryMaps;
	}

	public List<UserLeaveDetailsDto> getUserLeaveDetails() {
		return userLeaveDetails;
	}

	public void setUserLeaveDetailsDtoList(List<UserLeaveDetailsDto> userLeaveDetails) {
		this.userLeaveDetails = userLeaveDetails;
	}

}
