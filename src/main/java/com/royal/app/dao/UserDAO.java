package com.royal.app.dao;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import com.royal.app.shared.dto.TokenDetailsDto;
import com.royal.app.shared.dto.UserDto;
import com.royal.app.shared.dto.UserInventoryMapDto;

public interface UserDAO {
	
	Optional<UserDto> findByUsername(String username) throws Exception;
	
	Boolean existsByUsername(String username) throws Exception;
	
    Boolean existsByEmail(String email) throws Exception;
    
    public UserDto save(UserDto userDto) throws Exception;
    
    public List<UserDto> getUsersList(String roleName) throws Exception;
    
    public boolean UpdateUser(UserDto userDto) throws Exception;
    
    public boolean UpdateUserStatus(UserDto userDto) throws Exception; 
    
	public UserDto getSuperVisorUsersList() throws Exception;  
/*	
	public AgentDto getAgentList(AgentDto agentDto) throws Exception;
	
	public AgentDto getAgentDetList(AgentDto agentDto) throws Exception;*/
	
	public UserDto getModuleScreenDet(String employeeId) throws Exception;
	
	//public AgentDto getRoleAndInventoryByUsersList(AgentDto agentDto, String roleName) throws Exception;

	Object[] saveOrUpdateLoginDetails(boolean insertFlag, Object[] loginInfo) throws Exception;
	
	public boolean resetPassword(UserDto userDto) throws Exception;

	List<UserInventoryMapDto> getUserInventoryMapList(BigInteger userDetailsId) throws Exception;

	public BigInteger findUserDetailIdUsername(String username) throws Exception; 
	
	public TokenDetailsDto fetchTokenDetails(TokenDetailsDto tokenDetailsDto) throws Exception;
	
	public TokenDetailsDto saveTokenDetails(TokenDetailsDto tokenDetailsDto) throws Exception;

	boolean checkExistingTokenDetails(TokenDetailsDto tokenDetailsDto) throws Exception;
	
	public TokenDetailsDto updateTokenStatus(TokenDetailsDto tokenDetailsDto) throws Exception;
	
	public UserDto getUserReportList(UserDto userDto) throws Exception;
	
}
