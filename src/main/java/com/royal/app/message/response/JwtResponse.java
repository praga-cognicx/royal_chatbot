package com.royal.app.message.response;

import java.sql.Timestamp;
public class JwtResponse {
	private String userName;
    private String token;
    private String type = "Bearer";
    private String roles;
    private String expirySeconds;
    private Timestamp expiryDate;
    private String firstName;
    private String lastName;
    private String mobileNumber;
 
    public JwtResponse() {}
    
    public JwtResponse(JwtResponse jwtResponse) {
    	this.userName = jwtResponse.getUserName();
    	this.token = jwtResponse.getAccessToken();
        this.roles = jwtResponse.getRoles();
        this.expirySeconds = jwtResponse.getExpirySeconds();
        this.expiryDate = jwtResponse.getExpiryDate();
        this.firstName = jwtResponse.getFirstName();
        this.lastName = jwtResponse.getLastName();
        this.mobileNumber = jwtResponse.getMobileNumber();
    }
 
	public String getAccessToken() {
        return token;
    }
 
    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }
 
    public String getTokenType() {
        return type;
    }
 
    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public String getExpirySeconds() {
		return expirySeconds;
	}

	public void setExpirySeconds(String expirySeconds) {
		this.expirySeconds = expirySeconds;
	}

	public Timestamp getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Timestamp expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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
}