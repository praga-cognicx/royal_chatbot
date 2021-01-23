/*package com.royal.app.controller;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.royal.app.configuration.SpringSecurityAuditorAware;
import com.royal.app.constants.StatusCodeConstants;
import com.royal.app.message.request.GetEmployeeIdsRequest;
import com.royal.app.message.request.ModuleScreenRequest;
import com.royal.app.message.request.SearchForm;
import com.royal.app.message.request.UserCenterRequest;
import com.royal.app.message.request.UserClientRequest;
import com.royal.app.message.request.UserCreateRequest;
import com.royal.app.message.request.UserProcessRequest;
import com.royal.app.message.request.UserRegionRequest;
import com.royal.app.message.request.UserStatusRequest;
import com.royal.app.message.request.UserUpdateRequest;
import com.royal.app.message.response.GenericResponse;
import com.royal.app.message.response.SupervisorsResponse;
import com.royal.app.message.response.UsersListResponse;
import com.royal.app.message.response.UsersResponse;
import com.royal.app.services.RolesService;
import com.royal.app.services.UserService;
import com.royal.app.services.impl.UserDetailsServiceImpl;
import com.royal.app.shared.dto.UserDto;
import com.royal.app.shared.dto.UserInventoryMapDto;
import com.royal.app.util.UserInfo;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UsersController {
	
	private Logger logger = Logger.getLogger(UsersController.class);

	@Autowired
	RolesService rolesService;
	    
	@Autowired
	UserService userService;
	
	@Autowired
	UserDetailsServiceImpl userDetailsService;
	
    @Autowired
    PasswordEncoder encoder;
    
    @Autowired
    SpringSecurityAuditorAware springSecurityAuditorAware;
    
    @Autowired
    UserInfo userInfo;
	   
	
	@PostMapping(path="/create")
	public ResponseEntity<GenericResponse> createUser(@Valid @RequestBody UserCreateRequest userCreateRequest)
			throws Exception {
		GenericResponse genericResponse = new GenericResponse();
		try {
			if (userDetailsService.existsByUsername(userCreateRequest.getEmployeeId())) {
				genericResponse.setStatus(StatusCodeConstants.FAILURE);
				genericResponse.setError(StatusCodeConstants.FAILURE_STR);
				genericResponse.setMessage("Employee Id is already taken.");
				genericResponse.setValue(null);
				return new ResponseEntity<GenericResponse>(new GenericResponse(genericResponse), HttpStatus.OK);
			}

			if (userDetailsService.existsByEmail(userCreateRequest.getEmail())) {
				genericResponse.setStatus(StatusCodeConstants.FAILURE);
				genericResponse.setError(StatusCodeConstants.FAILURE_STR);
				genericResponse.setMessage("Email is already in use.");
				genericResponse.setValue(null);
				return new ResponseEntity<GenericResponse>(new GenericResponse(genericResponse), HttpStatus.OK);
			}

			if (userService.getUserRoles() == null || userService.getUserRoles().isEmpty()
					|| !userService.getUserRoles().contains(userCreateRequest.getRolesName())) {
				genericResponse.setStatus(StatusCodeConstants.FAILURE);
				genericResponse.setError(StatusCodeConstants.FAILURE_STR);
				genericResponse.setMessage("RolesName incorrect. Enter correct rolesName!");
				genericResponse.setValue(null);
				return new ResponseEntity<GenericResponse>(new GenericResponse(genericResponse), HttpStatus.OK);
			}

			// Creating user's account
			UserDto userDto = new UserDto();
			BeanUtils.copyProperties(userCreateRequest, userDto);
			userDto.setPassword(encoder.encode(userCreateRequest.getPassword()));
			userDto.setAutogenRolesId(userCreateRequest.getRoleId());
			userDto.setCreatedBy(userInfo.getEmployeeId());
			
			List<UserInventoryMapDto> userInventoryMapList = new ArrayList<>();
			if(userCreateRequest.getUserInventoryMaps() != null) {
				for (UserRegionRequest userRegions : userCreateRequest.getUserInventoryMaps()) {
					for (UserCenterRequest userCenters : userRegions.getUserCenters()) {  
						for (UserClientRequest userClients : userCenters.getUserClients()) {
							for (UserProcessRequest userProcesses : userClients.getUserProcesses()) {
								UserInventoryMapDto userInventoryMapDto = new UserInventoryMapDto();
								userInventoryMapDto.setInventoryRegionId(userRegions.getInventoryRegionId());
								userInventoryMapDto.setInventoryRegionName(userRegions.getInventoryRegionName());
								userInventoryMapDto.setInventoryCenterId(userCenters.getInventoryCenterId());
								userInventoryMapDto.setInventoryCenterName(userCenters.getInventoryCenterName());
								userInventoryMapDto.setInventoryClientId(userClients.getInventoryClientId());
								userInventoryMapDto.setInventoryClientName(userClients.getInventoryClientName());
								userInventoryMapDto.setInventoryProcessId(userProcesses.getInventoryProcessId());
								userInventoryMapDto.setInventoryProcessName(userProcesses.getInventoryProcessName());
								userInventoryMapDto.setInventoryCategoryId(userProcesses.getInventoryCategoryId());
								userInventoryMapDto.setInventoryCategoryName(userProcesses.getInventoryCategoryName());
								userInventoryMapList.add(userInventoryMapDto);
							}
						}
					}
				}
				userDto.setUserInventoryMapDtoList(userInventoryMapList);
			}
			
			userDto = userService.save(userDto);
			if(userDto.getAutogenUsersId() != null) {
			  genericResponse.setStatus(StatusCodeConstants.SUCCESS);
			  genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
			  genericResponse.setMessage("User created successfully.");
			  genericResponse.setValue(new UsersResponse(userDto));
			} else {
			  genericResponse.setStatus(StatusCodeConstants.FAILURE);
			  genericResponse.setError(StatusCodeConstants.FAILURE_STR);
			  genericResponse.setMessage("Unable to create user. Please contact admin.");
			  genericResponse.setValue(null);
			}
			genericResponse.setStatus(StatusCodeConstants.SUCCESS);
			genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
			genericResponse.setMessage("User created successfully.");
			genericResponse.setValue(new UsersResponse(userDto));
		} catch (Exception e) {
			genericResponse.setStatus(StatusCodeConstants.FAILURE);
			genericResponse.setError(StatusCodeConstants.FAILURE_STR);
			genericResponse.setMessage("Unable to create user. Please contact admin.");
			genericResponse.setValue(null);
			logger.error("Exception::UsersController.Class::create()", e);
		}
		return ResponseEntity.ok().body(new GenericResponse(genericResponse));
	}
	    
    @PostMapping(path="/usersList")
	public ResponseEntity<GenericResponse> getUserList() throws Exception {

		GenericResponse genericResponse = new GenericResponse();
		try {
			List<UsersResponse> userList = userService.getUsersList();
			if (!userList.isEmpty()) {
				genericResponse.setStatus(StatusCodeConstants.SUCCESS);
				genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
				genericResponse.setMessage("User list found.");
				genericResponse.setValue(userList);
			} else {
				genericResponse.setStatus(StatusCodeConstants.SUCCESS);
				genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
				genericResponse.setValue(null);
				genericResponse.setMessage("User list not found.");
			}
		} catch (Exception e) {
			genericResponse.setStatus(StatusCodeConstants.FAILURE);
			genericResponse.setError(StatusCodeConstants.FAILURE_STR);
			genericResponse.setMessage("Unable to fetch user list. Please contact admin.");
			genericResponse.setValue(null);
			logger.error("Exception::UsersController.Class::getUserList()", e);
		}
		return ResponseEntity.ok(new GenericResponse(genericResponse));
	}    
    
    @PostMapping(path="/update")
	public ResponseEntity<GenericResponse> updateUser(@Valid @RequestBody UserUpdateRequest userUpdateRequest)
			throws Exception {
		GenericResponse genericResponse = new GenericResponse();
		try {
			UserDto userDto = new UserDto();
			BeanUtils.copyProperties(userUpdateRequest, userDto);
			userDto.setAutogenUsersId(userUpdateRequest.getId());
			userDto.setAutogenRolesId(userUpdateRequest.getRoleId());
			userDto.setReports(userUpdateRequest.getReports());
			userDto.setCreatedBy(userInfo.getEmployeeId());
			userDto.setUpdatedBy(userInfo.getEmployeeId());
			List<UserInventoryMapDto> userInventoryMapList = new ArrayList<>();
			if(userUpdateRequest.getUserInventoryMaps() != null) {
				for (UserRegionRequest userRegions : userUpdateRequest.getUserInventoryMaps()) {
					for (UserCenterRequest userCenters : userRegions.getUserCenters()) {  
						for (UserClientRequest userClients : userCenters.getUserClients()) {
							for (UserProcessRequest userProcesses : userClients.getUserProcesses()) {
								UserInventoryMapDto userInventoryMapDto = new UserInventoryMapDto();
								userInventoryMapDto.setInventoryRegionId(userRegions.getInventoryRegionId());
								userInventoryMapDto.setInventoryRegionName(userRegions.getInventoryRegionName());
								userInventoryMapDto.setInventoryCenterId(userCenters.getInventoryCenterId());
								userInventoryMapDto.setInventoryCenterName(userCenters.getInventoryCenterName());
								userInventoryMapDto.setInventoryClientId(userClients.getInventoryClientId());
								userInventoryMapDto.setInventoryClientName(userClients.getInventoryClientName());
								userInventoryMapDto.setInventoryProcessId(userProcesses.getInventoryProcessId());
								userInventoryMapDto.setInventoryProcessName(userProcesses.getInventoryProcessName());
								userInventoryMapDto.setInventoryCategoryId(userProcesses.getInventoryCategoryId());
								userInventoryMapDto.setInventoryCategoryName(userProcesses.getInventoryCategoryName());
								userInventoryMapList.add(userInventoryMapDto);
							}
						}
					}
				}
				userDto.setUserInventoryMapDtoList(userInventoryMapList);
			}
			
			if(userUpdateRequest.getUserLeaveDetails() != null) {
				userDto.setUserLeaveDetailsDtoList(userUpdateRequest.getUserLeaveDetails());
			}
			
			boolean updateStatus = userService.UpDateUser(userDto);
			if (updateStatus) {
				genericResponse.setStatus(StatusCodeConstants.SUCCESS);
				genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
				genericResponse.setMessage("User updated successfully.");
				genericResponse.setValue(null);
			} else {
				genericResponse.setStatus(StatusCodeConstants.FAILURE);
				genericResponse.setError(StatusCodeConstants.FAILURE_STR);
				genericResponse.setMessage("Unable to update user details. Please contact admin.");
				genericResponse.setValue(null);
			}
		} catch (Exception e) {
			genericResponse.setStatus(StatusCodeConstants.FAILURE);
			genericResponse.setError(StatusCodeConstants.FAILURE_STR);
			genericResponse.setMessage("Unable to update user details. Please contact admin.");
			genericResponse.setValue(null);
			logger.error("Exception::UsersController.Class::updateUser()", e);
		}
		return ResponseEntity.ok(new GenericResponse(genericResponse));
	}  
    
    @PostMapping(path="/userStatus")
	public ResponseEntity<GenericResponse> updateUserStatus(@Valid @RequestBody UserStatusRequest userStatusRequest)
			throws Exception {
		GenericResponse genericResponse = new GenericResponse();
		try {
			UserDto userDto = new UserDto();
			userDto.setAutogenUsersId(userStatusRequest.getId());
			userDto.setStatus(userStatusRequest.getStatus());
			userDto.setUpdatedBy(userInfo.getEmployeeId());
			boolean updateStatus = userService.updateUserStatus(userDto);
			if (updateStatus) {
				genericResponse.setStatus(StatusCodeConstants.SUCCESS);
				genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
				genericResponse
						.setMessage("User Status Changed to " + userStatusRequest.getStatus() + " successfully.");
				genericResponse.setValue(null);
			} else {
				genericResponse.setStatus(StatusCodeConstants.FAILURE);
				genericResponse.setError(StatusCodeConstants.FAILURE_STR);
				genericResponse.setMessage("Unable to update user status. Please contact admin.");
				genericResponse.setValue(null);
			}
		} catch (Exception e) {
			genericResponse.setStatus(StatusCodeConstants.FAILURE);
			genericResponse.setError(StatusCodeConstants.FAILURE_STR);
			genericResponse.setMessage("Unable to update user status. Please contact admin.");
			genericResponse.setValue(null);
			logger.error("Exception::UsersController.Class::updateUserStatus()", e);
		}
		return ResponseEntity.ok(new GenericResponse(genericResponse));
	}  
	   
    @PostMapping(path="/search")
	public ResponseEntity<GenericResponse> search(@RequestBody SearchForm searchRequest) throws Exception {
		GenericResponse genericResponse = new GenericResponse();
		try {
			List<UsersResponse> userList = userService.getUsersList();
			if (!userList.isEmpty()) {
				genericResponse.setStatus(StatusCodeConstants.SUCCESS);
				genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
				genericResponse.setMessage("Users found.");
				genericResponse.setValue(userList);
			} else {
				genericResponse.setStatus(StatusCodeConstants.SUCCESS);
				genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
				genericResponse.setMessage("Users not found.");
				genericResponse.setValue(null);
			}
		} catch (Exception e) {
			genericResponse.setStatus(StatusCodeConstants.FAILURE);
			genericResponse.setError(StatusCodeConstants.FAILURE_STR);
			genericResponse.setMessage("Unable to search Users. Please contact admin.");
			genericResponse.setValue(null);
			logger.error("Exception::UsersController.Class::search()", e);
		}
		return ResponseEntity.ok(new GenericResponse(genericResponse));
	}
    
    @PostMapping(path="/getRoles")
	public ResponseEntity<GenericResponse> getRoles() throws Exception {
		GenericResponse genericResponse = new GenericResponse();
		try {
			UserDto rolesList = rolesService.getRoles();
			if (rolesList.getResultObj() != null) {
				genericResponse.setStatus(StatusCodeConstants.SUCCESS);
				genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
				genericResponse.setMessage("Roles found.");
				genericResponse.setValue(rolesList.getResultObj());
			} else {
				genericResponse.setStatus(StatusCodeConstants.SUCCESS);
				genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
				genericResponse.setMessage("Roles not found.");
				genericResponse.setValue(null);
			}
		} catch (Exception e) {
			genericResponse.setStatus(StatusCodeConstants.FAILURE);
			genericResponse.setError(StatusCodeConstants.FAILURE_STR);
			genericResponse.setMessage("Unable to fetch Roles. Please contact admin.");
			genericResponse.setValue(null);
			logger.error("Exception::UsersController.Class::getRoles()", e);
		}
		return ResponseEntity.ok(new GenericResponse(genericResponse));
	}
    
    @PostMapping(path="/getSupervisors")
	public ResponseEntity<GenericResponse> getSupervisors() throws Exception {
		GenericResponse genericResponse = new GenericResponse();
		try {
			List<SupervisorsResponse> SupervisorsList = userService.getSupervisorsList();
			if (!SupervisorsList.isEmpty()) {
				genericResponse.setStatus(StatusCodeConstants.SUCCESS);
				genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
				genericResponse.setMessage("Supervisors found.");
				genericResponse.setValue(SupervisorsList);
			} else {
				genericResponse.setStatus(StatusCodeConstants.SUCCESS);
				genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
				genericResponse.setMessage("Supervisors not found.");
				genericResponse.setValue(null);
			}
		} catch (Exception e) {
			genericResponse.setStatus(StatusCodeConstants.FAILURE);
			genericResponse.setError(StatusCodeConstants.FAILURE_STR);
			genericResponse.setMessage("Unable to fetch supervisor list. Please contact admin.");
			genericResponse.setValue(null);
			logger.error("Exception::UsersController.Class::getSupervisors()", e);
		}
		return ResponseEntity.ok(new GenericResponse(genericResponse));
	}
    
    @PostMapping(path="/agent/getAgentsList")
	public ResponseEntity<GenericResponse> getAgentList(@Valid @RequestBody AgentSearchRequest agentSearchRequest)
			throws Exception {
		GenericResponse genericResponse = new GenericResponse();
		try {
			AgentDto agentDto = new AgentDto();
			BeanUtils.copyProperties(agentSearchRequest, agentDto);
			List<AgentResponse> agentsList = userService.getAgentList(agentDto);
			if (!agentsList.isEmpty()) {
				genericResponse.setStatus(StatusCodeConstants.SUCCESS);
				genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
				genericResponse.setMessage("Agents found.");
				genericResponse.setValue(agentsList);
			} else {
				genericResponse.setStatus(StatusCodeConstants.SUCCESS);
				genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
				genericResponse.setMessage("Agents not found.");
				genericResponse.setValue(null);
			}
		} catch (Exception e) {
			genericResponse.setStatus(StatusCodeConstants.FAILURE);
			genericResponse.setError(StatusCodeConstants.FAILURE_STR);
			genericResponse.setMessage("Unable to fetch agents. Please contact admin.");
			genericResponse.setValue(null);
			logger.error("Exception::UsersController.Class::getAgentList()", e);
		}
		return ResponseEntity.ok(new GenericResponse(genericResponse));
	}
    
    @PostMapping(path="/agent/mapping")
	public ResponseEntity<GenericResponse> agentMapping(@RequestBody AgentCreateRequest agentCreateRequest)
			throws Exception {
		GenericResponse genericResponse = new GenericResponse();
		try {
			AgentDto agentDto = new AgentDto();
			agentDto.setFlag(false);
			BeanUtils.copyProperties(agentCreateRequest, agentDto);
			agentDto = userService.agentMapping(agentDto);
			if (agentDto.isFlag()) {
				genericResponse.setStatus(StatusCodeConstants.SUCCESS);
				genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
				genericResponse.setMessage("Agent mapped successfully.");
				genericResponse.setValue(null);
			} else {
				genericResponse.setStatus(StatusCodeConstants.FAILURE);
				genericResponse.setError(StatusCodeConstants.FAILURE_STR);
				genericResponse.setMessage("Unable to map agents. Please contact admin.");
				genericResponse.setValue(null);
			}
		} catch (Exception e) {
			genericResponse.setStatus(StatusCodeConstants.FAILURE);
			genericResponse.setError(StatusCodeConstants.FAILURE_STR);
			genericResponse.setMessage("Unable to mapping agents. Please contact admin.");
			genericResponse.setValue(null);
			logger.error("Exception::UsersController.Class::agentMapping()", e);
		}
		return ResponseEntity.ok(new GenericResponse(genericResponse));
	}
    
    @PostMapping(path="/agent/auditorList")
	public ResponseEntity<GenericResponse> getAuditorList() throws Exception {
		GenericResponse genericResponse = new GenericResponse();
		try {
			List<UsersResponse> userList = userService.getAuditorList();
			if (!userList.isEmpty()) {
				genericResponse.setStatus(StatusCodeConstants.SUCCESS);
				genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
				genericResponse.setMessage("Auditors list found.");
				genericResponse.setValue(userList);
			} else {
				genericResponse.setStatus(StatusCodeConstants.SUCCESS);
				genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
				genericResponse.setMessage("Auditor list not found.");
				genericResponse.setValue(null);
			}
		} catch (Exception e) {
			genericResponse.setStatus(StatusCodeConstants.FAILURE);
			genericResponse.setError(StatusCodeConstants.FAILURE_STR);
			genericResponse.setMessage("Unable to fetch auditor list. Please contact admin.");
			genericResponse.setValue(null);
			logger.error("Exception::UsersController.Class::getAuditorList()", e);

		}
		return ResponseEntity.ok(new GenericResponse(genericResponse));
	}   
    
    @PostMapping(path="/getEmployeeIdsByRole")
	public ResponseEntity<GenericResponse> getEmployeeIdsByRole(
			@Valid @RequestBody GetEmployeeIdsRequest getEmployeeIdsRequest) throws Exception {
		GenericResponse genericResponse = new GenericResponse();
		try {
			if (userService.getUserRoles() == null || userService.getUserRoles().isEmpty()
					|| !userService.getUserRoles().contains(getEmployeeIdsRequest.getRolesName())) {
				genericResponse.setStatus(StatusCodeConstants.FAILURE);
				genericResponse.setError(StatusCodeConstants.FAILURE_STR);
				genericResponse.setMessage("RolesName incorrect. Enter correct rolesName.");
				genericResponse.setValue(null);
				return new ResponseEntity<GenericResponse>(new GenericResponse(genericResponse), HttpStatus.OK);
			}
			List<UsersListResponse> userList = userService.getEmployeeIdsByRole(getEmployeeIdsRequest);

			if (!userList.isEmpty()) {
				genericResponse.setStatus(StatusCodeConstants.SUCCESS);
				genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
				genericResponse.setMessage(getEmployeeIdsRequest.getRolesName() + " Role: Users found.");
				genericResponse.setValue(userList);
			} else {
				genericResponse.setStatus(StatusCodeConstants.SUCCESS);
				genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
				genericResponse.setMessage(getEmployeeIdsRequest.getRolesName() + " Role: Users not found.");
				genericResponse.setValue(null);
			}
		} catch (Exception e) {
			genericResponse.setStatus(StatusCodeConstants.FAILURE);
			genericResponse.setError(StatusCodeConstants.FAILURE_STR);
			genericResponse.setMessage("Unable to fetch users list. Please contact admin.");
			genericResponse.setValue(null);
			logger.error("Exception::UsersController.Class::getEmployeeIdsByRole()", e);
		}
		return ResponseEntity.ok(new GenericResponse(genericResponse));
	}   
    
    @PostMapping(path="/agent/getAgentDetList")
	public ResponseEntity<GenericResponse> getAgentDetListByEmployeeIds(
			@Valid @RequestBody AgentDetailRequest agentDetailRequest) throws Exception {
		GenericResponse genericResponse = new GenericResponse();
		try {
			AgentDto agentDto = new AgentDto();
			agentDto.setResultObj(agentDetailRequest.getAgents());
			agentDto = userService.getAgentDetList(agentDto);
			if (agentDto.getResultObj() != null) {
				genericResponse.setStatus(StatusCodeConstants.SUCCESS);
				genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
				genericResponse.setMessage("Agents found!");
				genericResponse.setValue(agentDto.getResultObj());
			} else {
				genericResponse.setStatus(StatusCodeConstants.SUCCESS);
				genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
				genericResponse.setMessage("Agents not found.");
				genericResponse.setValue(null);
			}
		} catch (Exception e) {
			genericResponse.setStatus(StatusCodeConstants.FAILURE);
			genericResponse.setError(StatusCodeConstants.FAILURE_STR);
			genericResponse.setMessage("Unable to fetch agent list. Please contact admin.");
			genericResponse.setValue(null);
			logger.error("Exception::UsersController.Class::getAgentDetListByEmployeeIds()", e);
		}
		return ResponseEntity.ok(new GenericResponse(genericResponse));
	} 
    
    @PostMapping(path="/getModuleScreenList")
	public ResponseEntity<GenericResponse> getModuleScreenDetList(
			@Valid @RequestBody ModuleScreenRequest moduleScreenRequest) throws Exception {
		GenericResponse genericResponse = new GenericResponse();
		try {
			UserDto userDto = new UserDto();
			userDto = userService.getModuleScreenDet(moduleScreenRequest.getEmployeeId());

			if (userDto.getResultObj() != null) {
				genericResponse.setStatus(StatusCodeConstants.SUCCESS);
				genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
				genericResponse.setMessage("User module screens found.");
				genericResponse.setValue(userDto.getResultObj());
			} else {
				genericResponse.setStatus(StatusCodeConstants.SUCCESS);
				genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
				genericResponse.setMessage("User module screens not found.");
				genericResponse.setValue(null);
			}
		} catch (Exception e) {
			genericResponse.setStatus(StatusCodeConstants.FAILURE);
			genericResponse.setError(StatusCodeConstants.FAILURE_STR);
			genericResponse.setMessage("Unable to fetch user module screens list. Please contact admin.");
			genericResponse.setValue(null);
			logger.error("Exception::UsersController.Class::getModuleScreenDetList()", e);
		}
		return ResponseEntity.ok(new GenericResponse(genericResponse));
	} 
    
    @PostMapping(path="/QA/getQANameList")
	public ResponseEntity<GenericResponse> getQANameList(@Valid @RequestBody QASearchRequest qaSearchRequest)
			throws Exception {
		GenericResponse genericResponse = new GenericResponse();
		try {
			AgentDto agentDto = new AgentDto();
			BeanUtils.copyProperties(qaSearchRequest, agentDto);
			List<QANameListResponse> agentsList = userService.getQANameList(agentDto);
			if (!agentsList.isEmpty()) {
				genericResponse.setStatus(StatusCodeConstants.SUCCESS);
				genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
				genericResponse.setMessage("QA Names found.");
				genericResponse.setValue(agentsList);
			} else {
				genericResponse.setStatus(StatusCodeConstants.SUCCESS);
				genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
				genericResponse.setMessage("QA Names not found!");
				genericResponse.setValue(null);
			}
		} catch (Exception e) {
			genericResponse.setStatus(StatusCodeConstants.FAILURE);
			genericResponse.setError(StatusCodeConstants.FAILURE_STR);
			genericResponse.setMessage("Unable to fetch QA Name list. Please contact admin.");
			genericResponse.setValue(null);
			logger.error("Exception::UsersController.Class::getQANameList()", e);
		}
		return ResponseEntity.ok(new GenericResponse(genericResponse));
	}
    
    @PostMapping(path="/userInventoryMaps")
	public ResponseEntity<GenericResponse> getUserInventoryMapsList()
			throws Exception {
		GenericResponse genericResponse = new GenericResponse();
		try {
			List<UserRegionRequest> resultList = userService.getUserInventoryMapsList();
			if (resultList != null && !resultList.isEmpty()) {
				genericResponse.setStatus(StatusCodeConstants.SUCCESS);
				genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
				genericResponse.setMessage("User Inventory Maps found.");
				genericResponse.setValue(resultList);
			} else {
				genericResponse.setStatus(StatusCodeConstants.SUCCESS);
				genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
				genericResponse.setMessage("User Inventory Maps not found.");
				genericResponse.setValue(null);
			}
		} catch (Exception e) {
			genericResponse.setStatus(StatusCodeConstants.FAILURE);
			genericResponse.setError(StatusCodeConstants.FAILURE_STR);
			genericResponse.setMessage("Unable to User Inventory Maps. Please contact admin.");
			genericResponse.setValue(null);
			logger.error("Exception::UsersController.Class::getUserInventoryMapsList()", e);
		}
		return ResponseEntity.ok(new GenericResponse(genericResponse));
	}
    
}
*/