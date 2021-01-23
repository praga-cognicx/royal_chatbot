package com.royal.app.services;

import java.util.List;
import com.royal.app.message.request.GetEmployeeIdsRequest;
import com.royal.app.message.request.UserRegionRequest;
import com.royal.app.message.response.SupervisorsResponse;
import com.royal.app.message.response.UsersListResponse;
import com.royal.app.message.response.UsersResponse;
import com.royal.app.shared.dto.TokenDetailsDto;
import com.royal.app.shared.dto.UserDto;

public interface UserService {
	
	public TokenDetailsDto fetchTokenDetails(TokenDetailsDto tokenDetailsDto) throws Exception;
	
	public TokenDetailsDto saveTokenDetails(TokenDetailsDto tokenDetailsDto) throws Exception;
	
	boolean checkExistingTokenDetails(TokenDetailsDto tokenDetailsDto) throws Exception;
	
	public TokenDetailsDto updateTokenStatus(TokenDetailsDto tokenDetailsDto) throws Exception;
	
	public Object[] saveOrUpdateLoginDetails(boolean insertFlag, Object[] loginInfo) throws Exception;
	
	public UserDto save(UserDto userDto) throws Exception;

	public List<UsersResponse> getUsersList() throws Exception;
	
	public boolean UpDateUser(UserDto userDto) throws Exception;
	
	public List<SupervisorsResponse> getSupervisorsList() throws Exception; 
	
/*	public List<AgentResponse> getAgentList(AgentDto agentDto) throws Exception; */
	
	public List<UsersResponse> getAuditorList() throws Exception;
	
	public List<UsersListResponse> getEmployeeIdsByRole(GetEmployeeIdsRequest getEmployeeIdsRequest) throws Exception; 
	
/*	public AgentDto getAgentDetList(AgentDto agentDto) throws Exception;*/
	
	public boolean updateUserStatus(UserDto userDto)  throws Exception;
	
	public UserDto getModuleScreenDet(String employeeId) throws Exception;
	
	public List<String> getUserRoles() throws Exception;
	
/*	public List<QANameListResponse> getQANameList(AgentDto agentDto) throws Exception; */
	
	public boolean resetPassword(UserDto userDto) throws Exception;

	List<UserRegionRequest> getUserInventoryMapsList() throws Exception; 
	
	public UserDto getUserReportList(UserDto userDto) throws Exception;
}
