package com.royal.app.message.request;

import java.math.BigInteger;

public class UserStatusRequest {
	
	private BigInteger id;
	private String status;
	
	
	public BigInteger getId() {
		return id;
	}
	public void setId(BigInteger id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
