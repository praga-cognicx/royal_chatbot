package com.royal.app.message.response;

import java.util.List;

public class UserListResponse {
	
	private List<UsersResponse> usersResponseList;
	
	public UserListResponse(List<UsersResponse> usersResponseList) {
		this.usersResponseList = usersResponseList;
	}

	public List<UsersResponse> getUsersResponseList() {
		return usersResponseList;
	}

	public void setUsersResponseList(List<UsersResponse> usersResponseList) {
		this.usersResponseList = usersResponseList;
	}
	
	
	

}
