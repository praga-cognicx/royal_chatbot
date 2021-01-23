package com.royal.app.message.request;

import javax.validation.constraints.NotEmpty;

public class AuthenticateRequest {
	
	@NotEmpty
	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	
}
