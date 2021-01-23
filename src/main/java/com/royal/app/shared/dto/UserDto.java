package com.royal.app.shared.dto;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import com.royal.app.message.request.UserRegionRequest;
import com.royal.app.model.Reports;
import com.royal.app.model.Roles;
 
public class UserDto{
 
	private BigInteger autogenUsersId;
	private String autogenUsersDetailsId;
	private BigInteger inventoryCategoryId;
	private String inventoryCategoryName;
	private String email;
	private String employeeId;
	private String firstName;
	private String lastName;
	private int loginAttempt;
	private String mobileNumber;
	private String password;
	private Date recAddDt;
	private Date recUpdateDt;
	private String status;
	private String supervisorUsersName;
	private Set<Roles> roles = new HashSet<>();
	private List<UserInventoryMapDto> userInventoryMapDtoList = new ArrayList<>();
	private List<UserLeaveDetailsDto> userLeaveDetailsDtoList = new ArrayList<>();
	private List<UserRegionRequest> userInventoryMaps = new ArrayList<>();
	private List<Reports> reports = new ArrayList<>();
    private Collection<? extends GrantedAuthority> authorities;
    private String supervisorUsersId;    
    private BigInteger autogenRolesId;
    private String createdBy;
    private String updatedBy;
    private String rolesName;
    private List<Object[]> resultObjList;
    private Object resultObj;
    public UserDto() {}

	public UserDto(UserDto userDto) {
		this.autogenUsersId = userDto.autogenUsersId;
		this.autogenUsersDetailsId = userDto.autogenUsersDetailsId;
		this.inventoryCategoryId = userDto.inventoryCategoryId;
		this.inventoryCategoryName = userDto.inventoryCategoryName;
		this.email = userDto.email;
		this.employeeId = userDto.employeeId;
		this.firstName = userDto.firstName;
		this.lastName = userDto.lastName;
		this.loginAttempt = userDto.loginAttempt;
		this.mobileNumber = userDto.mobileNumber;
		this.password = userDto.password;
		this.recAddDt = userDto.recAddDt;
		this.recUpdateDt = userDto.recUpdateDt;
		this.status = userDto.status;
		this.supervisorUsersName = userDto.supervisorUsersName;
		this.roles = userDto.roles;
		this.userInventoryMapDtoList = userDto.userInventoryMapDtoList;
		this.userInventoryMaps = userDto.userInventoryMaps;
		this.userLeaveDetailsDtoList = userDto.userLeaveDetailsDtoList;
		this.authorities = userDto.getAuthorities();
		this.supervisorUsersId = userDto.getSupervisorUsersId();
		this.autogenRolesId = userDto.getAutogenRolesId();
		this.rolesName = userDto.getRolesName();
	}
    
    
    
	public BigInteger getAutogenUsersId() {
		return autogenUsersId;
	}
	public void setAutogenUsersId(BigInteger autogenUsersId) {
		this.autogenUsersId = autogenUsersId;
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
	public int getLoginAttempt() {
		return loginAttempt;
	}
	public void setLoginAttempt(int loginAttempt) {
		this.loginAttempt = loginAttempt;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Set<Roles> getRoles() {
		return roles;
	}
	public void setRoles(Set<Roles> roles) {
		this.roles = roles;
	}
	
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}
	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}
	public String getSupervisorUsersId() {
		return supervisorUsersId;
	}

	public void setSupervisorUsersId(String supervisorUsersId) {
		this.supervisorUsersId = supervisorUsersId;
	}

	public String getAutogenUsersDetailsId() {
		return autogenUsersDetailsId;
	}

	public void setAutogenUsersDetailsId(String autogenUsersDetailsId) {
		this.autogenUsersDetailsId = autogenUsersDetailsId;
	}

	public BigInteger getInventoryCategoryId() {
		return inventoryCategoryId;
	}

	public void setInventoryCategoryId(BigInteger inventoryCategoryId) {
		this.inventoryCategoryId = inventoryCategoryId;
	}

	public BigInteger getAutogenRolesId() {
		return autogenRolesId;
	}

	public void setAutogenRolesId(BigInteger autogenRolesId) {
		this.autogenRolesId = autogenRolesId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getRolesName() {
		return rolesName;
	}

	public void setRolesName(String rolesName) {
		this.rolesName = rolesName;
	}
	
	public String getInventoryCategoryName() {
		return inventoryCategoryName;
	}

	public void setInventoryCategoryName(String inventoryCategoryName) {
		this.inventoryCategoryName = inventoryCategoryName;
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

	public List<Object[]> getResultObjList() {
		return resultObjList;
	}

	public void setResultObjList(List<Object[]> resultObjList) {
		this.resultObjList = resultObjList;
	}

	public Object getResultObj() {
		return resultObj;
	}

	public void setResultObj(Object resultObj) {
		this.resultObj = resultObj;
	}

	public List<UserInventoryMapDto> getUserInventoryMapDtoList() {
		return userInventoryMapDtoList;
	}

	public void setUserInventoryMapDtoList(List<UserInventoryMapDto> userInventoryMapDtoList) {
		this.userInventoryMapDtoList = userInventoryMapDtoList;
	}

	public List<UserRegionRequest> getUserInventoryMaps() {
		return userInventoryMaps;
	}

	public void setUserInventoryMaps(List<UserRegionRequest> userInventoryMaps) {
		this.userInventoryMaps = userInventoryMaps;
	}

	public List<UserLeaveDetailsDto> getUserLeaveDetailsDtoList() {
		return userLeaveDetailsDtoList;
	}

	public void setUserLeaveDetailsDtoList(List<UserLeaveDetailsDto> userLeaveDetailsDtoList) {
		this.userLeaveDetailsDtoList = userLeaveDetailsDtoList;
	}
	
}
