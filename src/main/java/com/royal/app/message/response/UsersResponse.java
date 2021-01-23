package com.royal.app.message.response;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import com.royal.app.message.request.UserRegionRequest;
import com.royal.app.model.Reports;
import com.royal.app.shared.dto.UserDto;
import com.royal.app.shared.dto.UserLeaveDetailsDto;

public class UsersResponse {
	    
	private BigInteger id;
	private BigInteger inventoryCategoryId;
	private String inventoryCategoryName;
	private String email;
	private String employeeId;
	private String firstName;
	private String lastName;
	private String mobileNumber;
	private String status;
	private String supervisorUsersName;
	private List<UserRegionRequest> userInventoryMaps = new ArrayList<>();
	private List<UserLeaveDetailsDto> userLeaveDetails = new ArrayList<>();
	private List<Reports> reports = new ArrayList<>();
    private String supervisorUsersId;    
    private BigInteger roleId;
    private String rolesName;
    
    public UsersResponse() {}

	public UsersResponse(UserDto userDto) {
		this.id = userDto.getAutogenUsersId();
		this.inventoryCategoryId = userDto.getInventoryCategoryId();
		this.inventoryCategoryName = userDto.getInventoryCategoryName();
		this.email = userDto.getEmail();
		this.employeeId = userDto.getEmployeeId();
		this.firstName = userDto.getFirstName();
		this.lastName = userDto.getLastName();
		this.mobileNumber = userDto.getMobileNumber();
		this.status = userDto.getStatus();
		this.supervisorUsersName = userDto.getSupervisorUsersName();
		this.userInventoryMaps = userDto.getUserInventoryMaps();
		this.reports = userDto.getReports();
		this.supervisorUsersId = userDto.getSupervisorUsersId();
		this.inventoryCategoryName = userDto.getInventoryCategoryName();
		this.roleId = userDto.getAutogenRolesId();
		this.rolesName = userDto.getRolesName();
		this.userLeaveDetails = userDto.getUserLeaveDetailsDtoList();
	}
	
	public UsersResponse(BigInteger id, String employeeId) {
		this.id = id;
		this.employeeId = employeeId;
	}

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

	public String getInventoryCategoryName() {
		return inventoryCategoryName;
	}

	public void setInventoryCategoryName(String inventoryCategoryName) {
		this.inventoryCategoryName = inventoryCategoryName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSupervisorUsersName() {
		return supervisorUsersName;
	}

	public void setSupervisorUsersName(String supervisorUsersName) {
		this.supervisorUsersName = supervisorUsersName;
	}

	public List<Reports> getReports() {
		return reports;
	}

	public void setReports(List<Reports> reports) {
		this.reports = reports;
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

	public String getRolesName() {
		return rolesName;
	}

	public void setRolesName(String rolesName) {
		this.rolesName = rolesName;
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

	public void setUserLeaveDetails(List<UserLeaveDetailsDto> userLeaveDetails) {
		this.userLeaveDetails = userLeaveDetails;
	}

	
}
