package com.royal.app.message.request;

import javax.validation.constraints.NotEmpty;

public class ForgetPasswordRequest {
	
	@NotEmpty
	private String employeeId;

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
}
