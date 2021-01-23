package com.royal.app.shared.dto;

import java.util.Date;

public class UserGroupDto {
	
	private String autogenUserGroupsId;
	private String groupId;
	private String groupName;
	private Date recAddDt;
	private Date recUpdateDt;
	private String status;
	
	public String getAutogenUserGroupsId() {
		return autogenUserGroupsId;
	}
	public void setAutogenUserGroupsId(String autogenUserGroupsId) {
		this.autogenUserGroupsId = autogenUserGroupsId;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
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
	
}
