package com.royal.app.message.request;

import javax.validation.constraints.NotEmpty;

public class GetEmployeeIdsRequest {
	
	@NotEmpty
	private String rolesName;

	public String getRolesName() {
		return rolesName;
	}

	public void setRolesName(String rolesName) {
		this.rolesName = rolesName;
	}

	
}
