package com.royal.app.services.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.royal.app.dao.UserDAO;
import com.royal.app.message.request.GetEmployeeIdsRequest;
import com.royal.app.message.request.UserCenterRequest;
import com.royal.app.message.request.UserClientRequest;
import com.royal.app.message.request.UserProcessRequest;
import com.royal.app.message.request.UserRegionRequest;
import com.royal.app.message.response.ModuleListResponse;
import com.royal.app.message.response.ReportResponse;
import com.royal.app.message.response.RolesResponse;
import com.royal.app.message.response.ScreenListResponse;
import com.royal.app.message.response.SupervisorsResponse;
import com.royal.app.message.response.UsersListResponse;
import com.royal.app.message.response.UsersResponse;
import com.royal.app.services.RolesService;
import com.royal.app.services.UserService;
import com.royal.app.shared.dto.TokenDetailsDto;
import com.royal.app.shared.dto.UserDto;
import com.royal.app.shared.dto.UserInventoryMapDto;
import com.royal.app.util.CommonUtil;
import com.royal.app.util.UserInfo;

@Service
public class UserServiceImpl implements UserService{


	UserDAO userDAO;
	
	@Autowired
	RolesService rolesService;
	
    @Autowired
    UserInfo userInfo;
	
	@Override
	public Object[] saveOrUpdateLoginDetails(boolean insertFlag, Object[] loginInfo) throws Exception {
		return userDAO.saveOrUpdateLoginDetails(insertFlag, loginInfo);
	}

	
	@Override
	public UserDto save(UserDto userDto) throws Exception {
		return userDAO.save(userDto);
	}

    //Utility function
    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor)
    {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
    
    private static <T> Predicate<T> distinctByKeys(Function<? super T, ?>... keyExtractors)
    {
      final Map<List<?>, Boolean> seen = new ConcurrentHashMap<>();
       
      return t ->
      {
        final List<?> keys = Arrays.stream(keyExtractors)
                    .map(ke -> ke.apply(t))
                    .collect(Collectors.toList());
         
        return seen.putIfAbsent(keys, Boolean.TRUE) == null;
      };
    }
	
	@Override
	public List<UsersResponse> getUsersList() throws Exception {
		List<UserDto> userList = userDAO.getUsersList("");
		List<UsersResponse> userResList = new ArrayList<>();
		for (UserDto userDto : userList) {
			
			List<UserInventoryMapDto> userInventoryMapList =  userDto.getUserInventoryMapDtoList();
			
			if(userInventoryMapList != null && !userInventoryMapList.isEmpty()) {
	        List<UserInventoryMapDto> regionList = userInventoryMapList.stream()
	                                            .filter(distinctByKey(region -> region.getInventoryRegionId()) )
	                                            .collect( Collectors.toList());
	        
	        List<UserRegionRequest> inventoryMapResponseList =  new ArrayList<>();
	        regionList.stream().forEach(regionObject -> {
	        	 UserRegionRequest inventoryMapResponse = new UserRegionRequest();
	        	
	        	 List<UserInventoryMapDto> centerList = userInventoryMapList.stream()
                         .filter(center -> center.getInventoryRegionId().equals(regionObject.getInventoryRegionId()))
                         .filter(distinctByKey(center -> center.getInventoryCenterId()))
                         .collect( Collectors.toList());
	        	 List<UserCenterRequest> userCenters = new ArrayList<>();
	        	 centerList.stream().forEach(centerObject ->{
	        		 
	        		 UserCenterRequest centerRequest = new UserCenterRequest();
	        		 List<UserInventoryMapDto> clientList = userInventoryMapList.stream()
	                         .filter(client -> client.getInventoryRegionId().equals(regionObject.getInventoryRegionId()) 
	                        		 && client.getInventoryCenterId().equals(centerObject.getInventoryCenterId()))
	                         .filter(distinctByKey(client -> client.getInventoryClientId()))
	                         .collect(Collectors.toList());
	        		 List<UserClientRequest> userClients = new ArrayList<>(); 
	        		 clientList.forEach(clientObject ->{
	        			 
	        			 UserClientRequest clientRequest = new UserClientRequest();
	        			 List<UserInventoryMapDto> processList = userInventoryMapList.stream()
		                         .filter(process -> process.getInventoryRegionId().equals(regionObject.getInventoryRegionId()) 
		                        		 && process.getInventoryCenterId().equals(centerObject.getInventoryCenterId())
		                        		 && process.getInventoryClientId().equals(clientObject.getInventoryClientId()))
		                         .filter(distinctByKey(process -> process.getInventoryProcessId()))
		                         .collect(Collectors.toList());
	        			 
	        			 List<UserProcessRequest> userProcesses = new ArrayList<>(); 
		        		 processList.forEach(processObject ->{
		        			 UserProcessRequest processRequest = new UserProcessRequest();
		        			 processRequest.setInventoryProcessId(processObject.getInventoryProcessId());
		        			 processRequest.setInventoryProcessName(processObject.getInventoryProcessName());
		        			 processRequest.setInventoryCategoryId(processObject.getInventoryCategoryId());
		        			 processRequest.setInventoryCategoryName(processObject.getInventoryCategoryName());
		        			 userProcesses.add(processRequest);
		        		 });
	        			 
		        		 clientRequest.setInventoryClientId(clientObject.getInventoryClientId());
		        		 clientRequest.setInventoryClientName(clientObject.getInventoryClientName());
		        		 clientRequest.setUserProcesses(userProcesses);
		        		 userClients.add(clientRequest);
	        		 });
	        		 centerRequest.setUserClients(userClients);
	        		 centerRequest.setInventoryCenterId(centerObject.getInventoryCenterId());
	        		 centerRequest.setInventoryCenterName(centerObject.getInventoryCenterName());
	        		 userCenters.add(centerRequest);
	        	 });
	        	 inventoryMapResponse.setUserCenters(userCenters);
	        	 inventoryMapResponse.setInventoryRegionId(regionObject.getInventoryRegionId());
	        	 inventoryMapResponse.setInventoryRegionName(regionObject.getInventoryRegionName());
	        	 inventoryMapResponseList.add(inventoryMapResponse);
	        });
	        
	        userDto.setUserInventoryMaps(inventoryMapResponseList);
			}
			userResList.add(new UsersResponse(userDto));
		}
		
		return userResList;
	}

	private Predicate<? super UserInventoryMapDto> distinctByKey(BigInteger inventoryRegionId, Object object) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean UpDateUser(UserDto userDto) throws Exception {
		return userDAO.UpdateUser(userDto);
	}

	@Override
	public List<SupervisorsResponse> getSupervisorsList() throws Exception {
		List<SupervisorsResponse> supervisorsResponseList = new ArrayList<>();
		UserDto userDto = userDAO.getSuperVisorUsersList();
		if(userDto.getResultObjList() != null && !userDto.getResultObjList().isEmpty()) {
			userDto.getResultObjList().stream().forEach(obj ->{
				supervisorsResponseList.add(new SupervisorsResponse(String.valueOf(obj[0]), String.valueOf(obj[1])));
			});
		}
		return supervisorsResponseList;
	}

/*	@Override
	public List<AgentResponse> getAgentList(AgentDto agentDto) throws Exception {
		agentDto = userDAO.getAgentList(agentDto);
		List<AgentResponse> agentList = new ArrayList<>();
		if (agentDto.getResultObjList() != null && !agentDto.getResultObjList().isEmpty()) {
			agentDto.getResultObjList().stream().forEach(agent -> {
				if(agent[2].getClass().getTypeName() != null && "java.math.BigInteger".equalsIgnoreCase(agent[2].getClass().getTypeName())) {
					//BigInteger agentMapId = agent[2] != null ? (BigInteger)agent[2] : null;
					agentList.add(new AgentResponse(CommonUtil.nullRemove(agent[0]), CommonUtil.nullRemove(agent[1]), true));
				} else {
					agentList.add(new AgentResponse(CommonUtil.nullRemove(agent[0]), CommonUtil.nullRemove(agent[1]), false));
				}
			});
		}
		return agentList;
	}*/

	@Override
	public List<UsersResponse> getAuditorList() throws Exception {
		List<UserDto> userList = userDAO.getUsersList("Supervisor - Agent/Team Lead");
		List<UsersResponse> userResList = new ArrayList<>();
		for (UserDto userDto : userList) {
			userDto.setUserInventoryMaps( marshallUserInventoryMaps(userDto.getUserInventoryMapDtoList()));
			userResList.add(new UsersResponse(userDto));
		}
		return userResList;
	}
	
	public List<UsersListResponse> getEmployeeIdsByRole(GetEmployeeIdsRequest getEmployeeIdsRequest) throws Exception {
		List<UserDto> userList = userDAO.getUsersList(getEmployeeIdsRequest.getRolesName());
		List<UsersListResponse> userResList = new ArrayList<>();
		for (UserDto userDto : userList) {
			userResList.add(new UsersListResponse(userDto.getAutogenRolesId(), userDto.getEmployeeId()));
		}
		return userResList;
	}
	
	/*public AgentDto getAgentDetList(AgentDto agentDto) throws Exception{
		agentDto =  userDAO.getAgentDetList(agentDto);
		List<AgentDetResponse> agentDetResponseList = new ArrayList<>();
		agentDto.getResultObjList().stream().forEach(obj ->{
			List<UserRegionRequest> inventoryMapResponseList =  new ArrayList<>();
			if(obj[1] != null) {
				List<Object[]> inventoryListObj = (List<Object[]>) obj[1];
							
	              List<Object[]> regionList = inventoryListObj.stream()
	                                                  .filter(distinctByKey(region -> region[0]) )
	                                                  .collect( Collectors.toList());
	              regionList.stream().forEach(regionObject -> {
	                   UserRegionRequest inventoryMapResponse = new UserRegionRequest();
	                  
	                   List<Object[]> centerList = inventoryListObj.stream()
	                           .filter(center -> CommonUtil.nullRemove(center[0]).equals(CommonUtil.nullRemove(regionObject[0])))
	                           .filter(distinctByKey(center -> CommonUtil.nullRemove(center[2])))
	                           .collect( Collectors.toList());
	                   List<UserCenterRequest> userCenters = new ArrayList<>();
	                   centerList.stream().forEach(centerObject ->{
	                       
	                       UserCenterRequest centerRequest = new UserCenterRequest();
	                       List<Object[]> clientList = inventoryListObj.stream()
	                               .filter(client -> CommonUtil.nullRemove(client[0]).equals(CommonUtil.nullRemove(regionObject[0])) 
	                                       && CommonUtil.nullRemove(client[2]).equals(CommonUtil.nullRemove(centerObject[2])))
	                               .filter(distinctByKey(client -> CommonUtil.nullRemove(client[4])))
	                               .collect(Collectors.toList());
	                       List<UserClientRequest> userClients = new ArrayList<>(); 
	                       clientList.forEach(clientObject ->{
	                           
	                           UserClientRequest clientRequest = new UserClientRequest();
	                           List<Object[]> processList = inventoryListObj.stream()
	                                   .filter(process -> CommonUtil.nullRemove(process[0]).equals(CommonUtil.nullRemove(regionObject[0])) 
	                                           && CommonUtil.nullRemove(process[2]).equals(CommonUtil.nullRemove(centerObject[2]))
	                                           && CommonUtil.nullRemove(process[4]).equals(CommonUtil.nullRemove(clientObject[4])))
	                                   .filter(distinctByKey(process -> CommonUtil.nullRemove(process[6])))
	                                   .collect(Collectors.toList());
	                           
	                           List<UserProcessRequest> userProcesses = new ArrayList<>(); 
	                           processList.forEach(processObject ->{
	                               UserProcessRequest processRequest = new UserProcessRequest();
	                               if(CommonUtil.nullRemove(processObject[6]) != null && !CommonUtil.nullRemove(processObject[6]).isEmpty()) {
	                               processRequest.setInventoryProcessId(new BigInteger(CommonUtil.nullRemove(processObject[6])));
	                               processRequest.setInventoryProcessName(CommonUtil.nullRemove(processObject[7]));
	                               }
	                               if(CommonUtil.nullRemove(processObject[8]) != null && !CommonUtil.nullRemove(processObject[8]).isEmpty()) {
	                               processRequest.setInventoryCategoryId(new BigInteger(CommonUtil.nullRemove(processObject[8])));
	                               processRequest.setInventoryCategoryName(CommonUtil.nullRemove(processObject[9]));
	                               }
	                               userProcesses.add(processRequest);
	                           });
	                           
	                           clientRequest.setInventoryClientId(new BigInteger(CommonUtil.nullRemove(clientObject[4])));
	                           clientRequest.setInventoryClientName(CommonUtil.nullRemove(clientObject[5]));
	                           clientRequest.setUserProcesses(userProcesses);
	                           userClients.add(clientRequest);
	                       });
	                       centerRequest.setUserClients(userClients);
	                       centerRequest.setInventoryCenterId(new BigInteger(CommonUtil.nullRemove(centerObject[2])));
	                       centerRequest.setInventoryCenterName(CommonUtil.nullRemove(centerObject[3]));
	                       userCenters.add(centerRequest);
	                   });
	                   inventoryMapResponse.setUserCenters(userCenters);
	                   inventoryMapResponse.setInventoryRegionId(new BigInteger(CommonUtil.nullRemove(regionObject[0])));
	                   inventoryMapResponse.setInventoryRegionName(CommonUtil.nullRemove(regionObject[1]));
	                   inventoryMapResponseList.add(inventoryMapResponse);
	              });
				
			}
			if(obj[0] != null) {
				Object[] agentDet = (Object[]) obj[0];
			agentDetResponseList.add(new AgentDetResponse(CommonUtil.nullRemove(agentDet[0]), CommonUtil.nullRemove(agentDet[1]), CommonUtil.nullRemove(agentDet[2]), CommonUtil.nullRemove(agentDet[3]), inventoryMapResponseList));
			}
			});
		agentDto.setResultObj(null);
		agentDto.setResultObj(agentDetResponseList);
		return agentDto;
		
	}*/

	@Override
	public boolean updateUserStatus(UserDto userDto) throws Exception {
		return userDAO.UpdateUserStatus(userDto);
	}

	@Override
	public UserDto getModuleScreenDet(String employeeId) throws Exception {
		List<ModuleListResponse> moduleListResponse = null;
		UserDto userDto = userDAO.getModuleScreenDet(employeeId);
		if (userDto != null && userDto.getResultObjList() != null && !userDto.getResultObjList().isEmpty()) {
			List<List<String>> uniqueModuleList = new ArrayList<>();
			String tempModuleUId = "";

			for (Object[] obj : userDto.getResultObjList()) {
				if (!tempModuleUId.equalsIgnoreCase(obj[0].toString())) {
					tempModuleUId = obj[0].toString();
					List<String> moduleList = new ArrayList<>();
					moduleList.add(tempModuleUId);
					moduleList.add(obj[1].toString());
					uniqueModuleList.add(moduleList);
				}
			}
			moduleListResponse = new ArrayList<>();
			for (List<String> moduleObj : uniqueModuleList) {
				
				List<ScreenListResponse> screenListResponse = null;
				String tempScreenUId = "";
				for (Object[] obj : userDto.getResultObjList()) {
					if (moduleObj.get(0).equalsIgnoreCase(obj[0].toString())) {
						if (tempScreenUId.equalsIgnoreCase(obj[0].toString())) {
							if(screenListResponse != null) {
							screenListResponse.add(
									new ScreenListResponse(obj[2].toString(), obj[3].toString(), obj[4].toString()));
							}
							} else {
							screenListResponse = new ArrayList<>();
							tempScreenUId = moduleObj.get(0);
							screenListResponse.add(
									new ScreenListResponse(obj[2].toString(), obj[3].toString(), obj[4].toString()));
						}
					}
				}
				moduleListResponse.add(new ModuleListResponse(moduleObj.get(0), moduleObj.get(1), screenListResponse));
			}

			userDto.setResultObj(moduleListResponse);
		}

		return userDto;
	}
	
	public List<String> getUserRoles() throws Exception {
		List<String> roleList = new ArrayList<>();
		UserDto userDto = rolesService.getRoles();
		if(userDto.getResultObj() != null) {
    	List<RolesResponse> roleResList = (List<RolesResponse>) userDto.getResultObj();
    	roleResList.stream().forEach(obj->{
    		roleList.add(obj.getRolesName());
    	});
		}
		
		return roleList;
	}

/*	@Override
	public List<QANameListResponse> getQANameList(AgentDto agentDto) throws Exception {
		agentDto = userDAO.getRoleAndInventoryByUsersList(agentDto, "QA");
		List<QANameListResponse> qaNameList = new ArrayList<>();
		if (agentDto.getResultObjList() != null && !agentDto.getResultObjList().isEmpty()) {
			agentDto.getResultObjList().stream().forEach(qaObj -> {
				qaNameList.add(new QANameListResponse(CommonUtil.nullRemove(qaObj[1]), CommonUtil.nullRemove(qaObj[0])));
				
			});
		}
		return qaNameList;
	}
*/

	@Override
	public boolean resetPassword(UserDto userDto) throws Exception {
		return userDAO.resetPassword(userDto);
	}

	@Override
	public List<UserRegionRequest> getUserInventoryMapsList() throws Exception {
		BigInteger userdetailId = null;
		if (userInfo.getAutogenUserDetailsId() == null) {
			userdetailId = userDAO.findUserDetailIdUsername(userInfo.getEmployeeId());
			userInfo.setAutogenUserDetailsId(userdetailId);
		}
		List<UserRegionRequest> inventoryMapResponseList = new ArrayList<>();
		if (userdetailId != null) {
			List<UserInventoryMapDto> userInventoryMapList = userDAO.getUserInventoryMapList(userdetailId);
			inventoryMapResponseList = marshallUserInventoryMaps(userInventoryMapList);
		}
		return inventoryMapResponseList;
	}
	
	public List<UserRegionRequest>  marshallUserInventoryMaps(List<UserInventoryMapDto> userInventoryMapList) {
		List<UserRegionRequest> inventoryMapResponseList = new ArrayList<>();
		if (userInventoryMapList != null && !userInventoryMapList.isEmpty()) {
			List<UserInventoryMapDto> regionList = userInventoryMapList.stream()
					.filter(distinctByKey(region -> region.getInventoryRegionId())).collect(Collectors.toList());

			regionList.stream().forEach(regionObject -> {
				UserRegionRequest inventoryMapResponse = new UserRegionRequest();

				List<UserInventoryMapDto> centerList = userInventoryMapList.stream()
						.filter(center -> center.getInventoryRegionId().equals(regionObject.getInventoryRegionId()))
						.filter(distinctByKey(center -> center.getInventoryCenterId()))
						.collect(Collectors.toList());
				List<UserCenterRequest> userCenters = new ArrayList<>();
				centerList.stream().forEach(centerObject -> {

					UserCenterRequest centerRequest = new UserCenterRequest();
					List<UserInventoryMapDto> clientList = userInventoryMapList.stream().filter(
							client -> client.getInventoryRegionId().equals(regionObject.getInventoryRegionId())
									&& client.getInventoryCenterId().equals(centerObject.getInventoryCenterId()))
							.filter(distinctByKey(client -> client.getInventoryClientId()))
							.collect(Collectors.toList());
					List<UserClientRequest> userClients = new ArrayList<>();
					clientList.forEach(clientObject -> {

						UserClientRequest clientRequest = new UserClientRequest();
						List<UserInventoryMapDto> processList = userInventoryMapList.stream()
								.filter(process -> process.getInventoryRegionId()
										.equals(regionObject.getInventoryRegionId())
										&& process.getInventoryCenterId()
												.equals(centerObject.getInventoryCenterId())
										&& process.getInventoryClientId()
												.equals(clientObject.getInventoryClientId()))
								.filter(distinctByKey(process -> process.getInventoryProcessId()))
								.collect(Collectors.toList());

						List<UserProcessRequest> userProcesses = new ArrayList<>();
						processList.forEach(processObject -> {
							UserProcessRequest processRequest = new UserProcessRequest();
							processRequest.setInventoryProcessId(processObject.getInventoryProcessId());
							processRequest.setInventoryProcessName(processObject.getInventoryProcessName());
							userProcesses.add(processRequest);
						});

						clientRequest.setInventoryClientId(clientObject.getInventoryClientId());
						clientRequest.setInventoryClientName(clientObject.getInventoryClientName());
						clientRequest.setUserProcesses(userProcesses);
						userClients.add(clientRequest);
					});
					centerRequest.setUserClients(userClients);
					centerRequest.setInventoryCenterId(centerObject.getInventoryCenterId());
					centerRequest.setInventoryCenterName(centerObject.getInventoryCenterName());
					userCenters.add(centerRequest);
				});
				inventoryMapResponse.setUserCenters(userCenters);
				inventoryMapResponse.setInventoryRegionId(regionObject.getInventoryRegionId());
				inventoryMapResponse.setInventoryRegionName(regionObject.getInventoryRegionName());
				inventoryMapResponseList.add(inventoryMapResponse);
			});
		}
		
		return inventoryMapResponseList;
	}


	public TokenDetailsDto fetchTokenDetails(TokenDetailsDto tokenDetailsDto) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


	public TokenDetailsDto saveTokenDetails(TokenDetailsDto tokenDetailsDto) throws Exception {
		return userDAO.saveTokenDetails(tokenDetailsDto);
	}

	public boolean checkExistingTokenDetails(TokenDetailsDto tokenDetailsDto) throws Exception {
		return userDAO.checkExistingTokenDetails(tokenDetailsDto);
	}


	@Override
	public TokenDetailsDto updateTokenStatus(TokenDetailsDto tokenDetailsDto) throws Exception {
		return userDAO.updateTokenStatus(tokenDetailsDto);
	}
	
	public UserDto getUserReportList(UserDto userDto) throws Exception {
		userDto = userDAO.getUserReportList(userDto);
		if (userDto.getResultObj() != null) {
			List<Object[]> userReportList = (List<Object[]>) userDto.getResultObj();
			if (userReportList != null && !userReportList.isEmpty()) {
				List<ReportResponse> reportResponseList = new ArrayList<>();
				userReportList.forEach(obj -> {
					reportResponseList.add(new ReportResponse(new BigInteger(obj[0].toString()),
							CommonUtil.nullRemove(obj[1]), CommonUtil.nullRemove(obj[2])));

				});
				userDto.setResultObj(reportResponseList);
			}
		}
		return userDto;

	}

}
