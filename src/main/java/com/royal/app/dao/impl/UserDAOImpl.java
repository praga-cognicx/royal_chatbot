package com.royal.app.dao.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.royal.app.constants.AppicationConstants;
import com.royal.app.dao.RolesDAO;
import com.royal.app.dao.UserDAO;
import com.royal.app.model.LoginDetails;
import com.royal.app.model.Reports;
import com.royal.app.model.Roles;
import com.royal.app.model.TokenDetails;
import com.royal.app.model.UserInventoryMap;
import com.royal.app.model.UserLeaveDetails;
import com.royal.app.model.UserReportMap;
import com.royal.app.model.Users;
import com.royal.app.model.UsersDetail;
import com.royal.app.shared.dto.TokenDetailsDto;
import com.royal.app.shared.dto.UserDto;
import com.royal.app.shared.dto.UserInventoryMapDto;
import com.royal.app.shared.dto.UserLeaveDetailsDto;
import com.royal.app.util.CommonUtil;
import com.royal.app.util.DateUtil;
import com.royal.app.util.UserInfo;

@Repository
public class UserDAOImpl implements UserDAO {

	@PersistenceContext(unitName = AppicationConstants.FIRST_PERSISTENCE_UNIT_NAME)
	public EntityManager firstEntityManager;

	@Autowired
	@Qualifier(AppicationConstants.FIRST_JDBC_TEMPLATE)
	JdbcTemplate firstJdbcTemplate;
	
	@Autowired
	RolesDAOImpl rolesDAOImpl;
	
	@Autowired
	RolesDAO rolesDAO;
	
	@Autowired
	UserInfo userInfo;

	private Logger logger = Logger.getLogger(UserDAOImpl.class);

	@SuppressWarnings("unchecked")
	@Override
	public Optional<UserDto> findByUsername(String username) throws Exception{
		StringBuilder sqlQry = null;
		Optional<UserDto> userOptional = null;
		UserDto userDto = new UserDto();
		List<Object[]> resultObj = null;
		try {
			sqlQry = new StringBuilder("SELECT US.AUTOGEN_USERS_ID, US.EMPLOYEE_ID, US.PASSWORD, US.FIRST_NAME, US.LAST_NAME, US.EMAIL, US.MOBILE_NUMBER, US.STATUS, US.ROLE FROM RATESHEET_USERS US WHERE EMPLOYEE_ID=:USERNAME");
			Query queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
			queryObj.setParameter(AppicationConstants.USERNAME_STR, username);
			resultObj = (List<Object[]>) queryObj.getResultList();

			if (resultObj != null && !resultObj.isEmpty()) {
				for (Object[] objects : resultObj) {
					userDto = new UserDto();
					userDto.setAutogenUsersId(new BigInteger(objects[0].toString()));
					userDto.setEmployeeId(CommonUtil.nullRemove(objects[1]));
					userDto.setPassword(CommonUtil.nullRemove(objects[2]));
					userDto.setFirstName(CommonUtil.nullRemove(objects[3]));
					userDto.setLastName(CommonUtil.nullRemove(objects[4]));
					userDto.setEmail(CommonUtil.nullRemove(objects[5]));							
					userDto.setMobileNumber(CommonUtil.nullRemove(objects[6]));
                    userDto.setStatus(CommonUtil.nullRemove(objects[7]));
					Set<Roles> roleset =  new HashSet<>();
					Roles roles = new Roles();
					roles.setRolesName(CommonUtil.nullRemove(objects[8]));
					roleset.add(roles);
					userDto.setRoles(roleset);
				}
			}
			userOptional = Optional.ofNullable(userDto);
		} catch (Exception e) {
			logger.info("Exception :: UserDAOImpl :: findByUsername() : " + e);
		} finally {
			firstEntityManager.close();
		}
		return userOptional;
	}

	@Override
	public BigInteger findUserDetailIdUsername(String username) throws Exception{
		StringBuilder sqlQry = null;
		List<Object[]> result = null;
		BigInteger userdetId = null;
		try {
			sqlQry = new StringBuilder("SELECT usd.AUTOGEN_USERS_DETAILS_ID FROM Users us, users_details usd WHERE us.employee_Id=:USERNAME and usd.AUTOGEN_USERS_ID=us.AUTOGEN_USERS_ID");
			Query queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
			queryObj.setParameter(AppicationConstants.USERNAME_STR, username);
			result = (List<Object[]>) queryObj.getResultList();
			if(result != null && !result.isEmpty()) {
					userdetId =  new BigInteger(CommonUtil.nullRemove(result.get(0)));
			}
		} catch (Exception e) {
			logger.info("Exception :: UserDAOImpl :: findUserDetailIdUsername() : " + e);
		} finally {
			firstEntityManager.close();
		}
		return userdetId;
	}
	
	@Override
	public Boolean existsByUsername(String username) throws Exception{
		StringBuilder sqlQry = null;
		boolean user = false;
		List<Object[]> result = null;
		try {
			sqlQry = new StringBuilder("SELECT 1 FROM USERS WHERE EMPLOYEE_ID=:USERNAME");
			Query queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
			queryObj.setParameter(AppicationConstants.USERNAME_STR, username);
			result =  queryObj.getResultList();
			if(!result.isEmpty()) {
				user = true;
			}
		} catch (Exception e) {
			logger.info("Exception :: UserDAOImpl :: existsByUsername() : " + e);
		} finally {
			firstEntityManager.close();
		}
		return user;
	}

	@Override
	public Boolean existsByEmail(String email) throws Exception{
		StringBuilder sqlQry = null;
		boolean user = false;
		List<Object[]> result = null;
		try {
			sqlQry = new StringBuilder("SELECT 1 FROM USERS WHERE EMAIL=:EMAIL");
			Query queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
			queryObj.setParameter("EMAIL", email);
			result =  queryObj.getResultList();
			if(!result.isEmpty()) {
				user = true;
			}
		} catch (Exception e) {
			logger.info("Exception :: UserDAOImpl :: existsByEmail() : " + e);
		} finally {
			firstEntityManager.close();
		}
		return user;
	}
	
	@Override
	@Transactional(value = "firstTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Object[] saveOrUpdateLoginDetails(boolean insertFlag, Object[] loginInfo) throws Exception {
		Object[] resultObj = new Object[5];
		StringBuilder sqlQry = null;
		// loginInfo[] 0-employeeId, 1-loginTime(boolean), 2-logouttime(boolean),
		// 3-noOfAttempt, 4-remarks, 5- createdBy, 6-Updated By
		boolean insertStatus = false;
		List<LoginDetails> result = null;
		try {
			if (insertFlag) {
				LoginDetails loginDetails = new LoginDetails();
				loginDetails.setEmployeeId(CommonUtil.nullRemove(loginInfo[0]));
				/**if (loginInfo[1] != null && (boolean) loginInfo[1]) {
					loginDetails.setLoginTime(new Date());
				}*/
				result = findLoginDetailsByEmployeeId(loginInfo);
				if (result != null && !result.isEmpty() && result.get(0) != null) {
					LoginDetails findLoginDetails = result.get(0);
					Date loginTime = findLoginDetails.getLoginTime();
					int diffInDays = (int) ((loginTime.getTime() - new Date().getTime()) / (1000 * 60 * 60 * 24));
					if (30 <= diffInDays) {
						updateUserStatusByEmployeeId(CommonUtil.nullRemove(loginInfo[0]), AppicationConstants.INACTIVE_STR,
								CommonUtil.nullRemove(loginInfo[6]));
						loginDetails.setRemarks(
								"Your account is inactive user not logged into the system last 30 consecutive days. please contact the system administrator");

					}
				}

				if (loginInfo[3] != null && (!BigInteger.ZERO.equals(loginInfo[3]))) {
					if (result != null && !result.isEmpty() && result.get(0) != null) {
						LoginDetails findLoginDetails = result.get(0);
						if (!BigInteger.ZERO.equals(findLoginDetails.getNoOfAttempt())) {
							BigInteger totalAttempt = findLoginDetails.getNoOfAttempt();
							BigInteger val = new BigInteger(CommonUtil.nullRemove(loginInfo[3]));
							if (totalAttempt != null) {
								totalAttempt = totalAttempt.add(val);
								loginDetails.setNoOfAttempt(totalAttempt);
							} else {
								loginDetails.setNoOfAttempt(val);
							}
							if (BigInteger.valueOf(5).equals(findLoginDetails.getNoOfAttempt())) {
								updateUserStatusByEmployeeId(CommonUtil.nullRemove(loginInfo[0]), "BLOCKED",
										CommonUtil.nullRemove(loginInfo[6]));
								loginDetails.setRemarks("Account blocked after 5 consecutive invalid login attempts.");
							}

						} else {
							loginDetails.setNoOfAttempt(new BigInteger(CommonUtil.nullRemove(loginInfo[3])));
						}
					} else {
						loginDetails.setNoOfAttempt(new BigInteger(CommonUtil.nullRemove(loginInfo[3])));
					}
				}
				if ((loginDetails.getRemarks() == null || loginDetails.getRemarks().isEmpty()) && loginInfo[4] != null
						&& !loginInfo[4].toString().isEmpty()) {
					loginDetails.setRemarks(loginInfo[4].toString());
				}
				if (loginInfo[5] != null && !loginInfo[5].toString().isEmpty()) {
					loginDetails.setCreatedBy(CommonUtil.nullRemove(loginInfo[5]));
				}
				loginDetails.setCreatedBy(userInfo.getEmployeeId());
				firstEntityManager.persist(loginDetails);
				insertStatus = true;
			} else {
				insertStatus = updateLoginDetails(loginInfo);
			}
		} catch (Exception e) {
			logger.info("Exception :: UserDAOImpl :: saveOrUpdateLoginDetails() : " + e);
		} finally {
			firstEntityManager.close();
		}
		resultObj[0] = insertStatus;
		return resultObj;
	}
	
	@Transactional(value = "firstTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateLoginDetails(Object[] loginInfo) throws Exception {
		List<LoginDetails> result = null;
		boolean insertStatus = false;
		try {
			result =  findLoginDetailsByEmployeeId(loginInfo);
			if(!result.isEmpty()) {
				LoginDetails loginDetails = result.get(0);
				if(loginInfo[2] != null && (boolean)loginInfo[2]) {
					loginDetails.setLogoutTime(new Date());
				}
				if(!CommonUtil.nullRemove(loginInfo[4]).isEmpty()) {
				loginDetails.setRemarks(CommonUtil.nullRemove(loginInfo[4]));
				}
				if(!CommonUtil.nullRemove(loginInfo[6]).isEmpty()) {
					loginDetails.setUpdatedBy(CommonUtil.nullRemove(loginInfo[6]));
				}
				insertStatus = true;
			}
		
		} catch (Exception e) {
			logger.info("Exception :: UserDAOImpl :: updateLoginDetails() : " + e);
		} finally {
			firstEntityManager.close();
		}
		return insertStatus;
	}
	
	public List<LoginDetails> findLoginDetailsByEmployeeId(Object[] loginInfo) throws Exception {
		StringBuilder sqlQry = null;
		List<LoginDetails> result = null;
		try {
			sqlQry = new StringBuilder("SELECT MAX(l) FROM LoginDetails l WHERE l.employeeId=:EMPLOYEEID");
			Query queryObj = firstEntityManager.createQuery(sqlQry.toString());
			queryObj.setParameter(AppicationConstants.EMPLOYEEID_STR, CommonUtil.nullRemove(loginInfo[0]));
			result =  queryObj.getResultList();
		} catch (Exception e) {
			logger.info("Exception :: UserDAOImpl :: findLoginDetailsByEmployeeId() : " + e);
		} finally {
			firstEntityManager.close();
		}
		return result;
	}
	

	@Override
	@Transactional(value = "firstTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public UserDto save(UserDto userDto) throws Exception{
		try {
			if(userDto != null) {
				Users users = new Users();
				BeanUtils.copyProperties(userDto, users);
				users.setStatus("NEW");
				firstEntityManager.persist(users);
				if(users.getAutogenUsersId() != null) {
					UsersDetail usersDet = new UsersDetail();
					BeanUtils.copyProperties(userDto, usersDet);
					usersDet.setAutogenUsersId(users.getAutogenUsersId());
					firstEntityManager.persist(usersDet);
					if (usersDet.getAutogenUsersDetailsId() != null) {
						if (userDto.getUserInventoryMapDtoList() != null) {
							for (UserInventoryMapDto userInventoryMapDto : userDto.getUserInventoryMapDtoList()) {
							  			UserInventoryMap userInventoryMap = new UserInventoryMap();
										userInventoryMap.setAutogenUsersDetailsId(usersDet.getAutogenUsersDetailsId());
										userInventoryMap.setInventoryRegionId(userInventoryMapDto.getInventoryRegionId());
										userInventoryMap.setInventoryRegionName(userInventoryMapDto.getInventoryRegionName());
										userInventoryMap.setInventoryCenterId(userInventoryMapDto.getInventoryCenterId());
									    userInventoryMap.setInventoryCenterName(userInventoryMapDto.getInventoryCenterName());
									    userInventoryMap.setInventoryClientId(userInventoryMapDto.getInventoryClientId());
									    userInventoryMap.setInventoryClientName(userInventoryMapDto.getInventoryClientName());
									    userInventoryMap.setInventoryProcessId(userInventoryMapDto.getInventoryProcessId());
									    userInventoryMap.setInventoryProcessName(userInventoryMapDto.getInventoryProcessName());
									    userInventoryMap.setInventoryCategoryId(userInventoryMapDto.getInventoryCategoryId());
									    userInventoryMap.setInventoryCategoryName(userInventoryMapDto.getInventoryCategoryName());
									    userInventoryMap.setStatus(AppicationConstants.ACTIVE_STR);
									    userInventoryMap.setCreatedBy(userDto.getCreatedBy());
									    firstEntityManager.persist(userInventoryMap);
									}
						}
					
						if (userDto.getReports() != null) {
							UserReportMap userReportMap = null;
							for (Reports report : userDto.getReports()) {
								userReportMap = new UserReportMap();
								userReportMap.setAutogenUsersDetailsId(usersDet.getAutogenUsersDetailsId());
								userReportMap.setAutogenReportMasterId(report.getId());
								userReportMap.setReportName(report.getReportName());
								userReportMap.setCreatedBy(userDto.getCreatedBy());
								userReportMap.setStatus(AppicationConstants.ACTIVE_STR);
								firstEntityManager.persist(userReportMap);
							}
						}
					
						if (userDto.getUserLeaveDetailsDtoList() != null) {
							for (UserLeaveDetailsDto userLeaveDetailsDto : userDto.getUserLeaveDetailsDtoList()) {
								UserLeaveDetails userLeaveDetails = new UserLeaveDetails();
								userLeaveDetails.setAutogenUsersDetailsId(usersDet.getAutogenUsersDetailsId());
								userLeaveDetails.setToDate(new Date());
								userLeaveDetails.setFromDate(new Date());
								userLeaveDetails.setNoOfDays(userLeaveDetailsDto.getNoOfDays());
								userLeaveDetails.setReasons(userLeaveDetailsDto.getReasons());
								userLeaveDetails.setComments(userLeaveDetailsDto.getComments());
								userLeaveDetails.setStatus(AppicationConstants.ACTIVE_STR);
								userLeaveDetails.setCreatedBy(userDto.getCreatedBy());
								firstEntityManager.persist(userLeaveDetails);
							}
						}
					
					}
					BeanUtils.copyProperties(usersDet, userDto);
				}
				BeanUtils.copyProperties(users, userDto);
			}
		} catch (Exception e) {
			logger.info("Exception :: UserDAOImpl :: save() : " + e);
		} finally {
			firstEntityManager.close();
		}
		
		return userDto;
	}

	@Override
	public List<UserDto> getUsersList(String roleName) throws Exception {
		StringBuilder sqlQry = null;
		List<Users> resultObj = new ArrayList<>();
		List<UserDto> userDtoList = new ArrayList<>();
		Query queryObj = null;
		try {
			sqlQry = new StringBuilder("SELECT us FROM Users us");
			if(!roleName.isEmpty()) {
				sqlQry = new StringBuilder("SELECT us FROM Users us, UsersDetail ud where us.status='Active' and ud.autogenUsersId=us.autogenUsersId and ud.autogenRolesId=(SELECT r.autogenRolesId FROM Roles r where r.rolesName='"+roleName+"')");
			}
			queryObj = firstEntityManager.createQuery(sqlQry.toString());
			resultObj =  queryObj.getResultList();
			for (Users users : resultObj) {
				UserDto userDto = new UserDto();
				BeanUtils.copyProperties(users, userDto);
				List<UsersDetail> subResultObj = null;
				sqlQry = new StringBuilder("SELECT usd FROM UsersDetail usd where usd.autogenUsersId=:USERID");
				queryObj = firstEntityManager.createQuery(sqlQry.toString());
				queryObj.setParameter("USERID", users.getAutogenUsersId());
				subResultObj =  queryObj.getResultList();
				for (UsersDetail users2 : subResultObj) {
					BeanUtils.copyProperties(users2, userDto);
					userDto.setRolesName(users2.getRolesName());
					
					queryObj = firstEntityManager.createQuery(
							"SELECT i FROM UserInventoryMap i WHERE autogenUsersDetailsId=:USERDETAILSID AND status='Active'");
					queryObj.setParameter("USERDETAILSID", users2.getAutogenUsersDetailsId());
						List<UserInventoryMap> userInventoryMapResult = queryObj.getResultList();
						List<UserInventoryMapDto> userInventoryMapDtos = new ArrayList<>();
						for (UserInventoryMap userInventoryMap : userInventoryMapResult) {
							UserInventoryMapDto userInventoryMapDto = new UserInventoryMapDto();
							BeanUtils.copyProperties(userInventoryMap, userInventoryMapDto);
							userInventoryMapDtos.add(userInventoryMapDto);
						}	
						userDto.setUserInventoryMapDtoList(userInventoryMapDtos);

					sqlQry = new StringBuilder("SELECT im.AUTOGEN_REPORT_MASTER_ID, im.REPORT_NAME from REPORT_MASTER im, USER_REPORT_MAP usd where im.AUTOGEN_REPORT_MASTER_ID = usd.AUTOGEN_REPORT_MASTER_ID AND usd.AUTOGEN_USERS_DETAILS_ID=:USERDETAILSID AND usd.STATUS=im.STATUS AND usd.STATUS='Active'");
					Query subQueryObj2 = firstEntityManager.createNativeQuery(sqlQry.toString());
					subQueryObj2.setParameter("USERDETAILSID", users2.getAutogenUsersDetailsId());
					List<Object[]> resultObjList =  subQueryObj2.getResultList();
					List<Reports> reports = new ArrayList<>();
					if (resultObjList != null && !resultObjList.isEmpty()) {
						for (Object[] userreportmap : resultObjList) {
							Reports report = new Reports();
							report.setId(new BigInteger((userreportmap[0].toString())));
							report.setReportName(String.valueOf(userreportmap[1]));
							reports.add(report);
						}
					}
					userDto.setReports(reports);
					
					List<UserLeaveDetails> userLeaveDetailsList = new ArrayList<>();
					sqlQry = new StringBuilder("SELECT im from UserLeaveDetails im where im.autogenUsersDetailsId=:USERDETAILSID AND im.status != 'INACTIVE'");
					subQueryObj2 = firstEntityManager.createQuery(sqlQry.toString());
					subQueryObj2.setParameter("USERDETAILSID", users2.getAutogenUsersDetailsId());
					userLeaveDetailsList =  subQueryObj2.getResultList();
					if(userLeaveDetailsList != null) {
					List<UserLeaveDetailsDto> uesrLeaveDetailsDtoList = new ArrayList<>();
					for ( UserLeaveDetails userLeaveDetails : userLeaveDetailsList) {
						UserLeaveDetailsDto userLeaveDetailsDto = new UserLeaveDetailsDto();
						userLeaveDetailsDto.setLeaveDetailsId(userLeaveDetails.getAutogenUserLeaveDetailsId());
						userLeaveDetailsDto.setFromDate(DateUtil.convertDatetoString(userLeaveDetails.getFromDate(), DateUtil.DATE_MONTH_YEAR_SLASH_PATTERN));
						userLeaveDetailsDto.setToDate(DateUtil.convertDatetoString(userLeaveDetails.getToDate(), DateUtil.DATE_MONTH_YEAR_SLASH_PATTERN));
						userLeaveDetailsDto.setNoOfDays(userLeaveDetails.getNoOfDays());
						userLeaveDetailsDto.setReasons(userLeaveDetails.getReasons());
						userLeaveDetailsDto.setComments(userLeaveDetails.getComments());
						userLeaveDetailsDto.setStatus(userLeaveDetails.getStatus());
						uesrLeaveDetailsDtoList.add(userLeaveDetailsDto);
					}
					userDto.setUserLeaveDetailsDtoList(uesrLeaveDetailsDtoList);
				}
				}
				userDtoList.add(userDto);
			}
		} catch (Exception e) {
			logger.info("Exception :: UserDAOImpl :: existsByEmail() : " + e);
		} finally {
			firstEntityManager.close();
		}
		return userDtoList ;
	}
	
	@Override
	public List<UserInventoryMapDto>  getUserInventoryMapList(BigInteger userDetailsId) throws Exception {
		Query queryObj = null;
		List<UserInventoryMapDto> userInventoryMapDtos = new ArrayList<>();
		try {
				List<Object[]> resultObjList = new ArrayList<>();
					queryObj = firstEntityManager.createQuery(
							"SELECT i FROM UserInventoryMap i WHERE autogenUsersDetailsId=:USERDETAILSID AND status='Active'");
					queryObj.setParameter("USERDETAILSID", userDetailsId);
						List<UserInventoryMap> userInventoryMapResult = queryObj.getResultList();
						for (UserInventoryMap userInventoryMap : userInventoryMapResult) {
							UserInventoryMapDto userInventoryMapDto = new UserInventoryMapDto();
							BeanUtils.copyProperties(userInventoryMap, userInventoryMapDto);
							userInventoryMapDtos.add(userInventoryMapDto);
						}	
		} catch (Exception e) {
			logger.info("Exception :: UserDAOImpl :: getUserInventoryMapList() : " + e);
		} finally {
			firstEntityManager.close();
		}
		return  userInventoryMapDtos;
	}
	
/**	@Override
	public List<UserInventoryMapDto>  getReportsByUser(String userDetailsId) throws Exception {
		Query queryObj = null;
		List<UserInventoryMapDto> userInventoryMapDtos = new ArrayList<>();
		try {
				List<Object[]> resultObjList = new ArrayList<>();
					queryObj = firstEntityManager.createQuery(
							"SELECT i FROM UserInventoryMap i WHERE autogenUsersDetailsId=:USERDETAILSID AND status='Active'");
					queryObj.setParameter("USERDETAILSID", userDetailsId);
						List<UserInventoryMap> userInventoryMapResult = queryObj.getResultList();
						for (UserInventoryMap userInventoryMap : userInventoryMapResult) {
							UserInventoryMapDto userInventoryMapDto = new UserInventoryMapDto();
							BeanUtils.copyProperties(userInventoryMap, userInventoryMapDto);
							userInventoryMapDtos.add(userInventoryMapDto);
						}	
		} catch (Exception e) {
			logger.info("Exception :: UserDAOImpl :: getUserInventoryMapList() : " + e);
		} finally {
			firstEntityManager.close();
		}
		return  userInventoryMapDtos;
	}*/
	
	@Transactional(value = "firstTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean UpdateUserStatus(UserDto userDto) throws Exception {
		boolean result = false;
		try {
			Users user = firstEntityManager.find(Users.class, userDto.getAutogenUsersId());
			if(user != null) {
				if(!CommonUtil.nullRemove(userDto.getStatus()).isEmpty()) {
					user.setStatus(userDto.getStatus());
				}
				if(!CommonUtil.nullRemove(userDto.getUpdatedBy()).isEmpty()) {
					user.setUpdatedBy(userDto.getUpdatedBy());
				}
				firstEntityManager.merge(user);
				result = true;
			}
		} catch (Exception e) {
			logger.info("Exception :: UserDAOImpl :: UpdateUserStatus() : " + e);
		} finally {
			firstEntityManager.close();
		}
		
		return result;
	}
	
	@Transactional(value = "firstTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateUserStatusByEmployeeId(String employeeId, String status, String updatedBy) throws Exception {
		List<Users> usersList = null;
		boolean result = false;
		try {
			StringBuilder sqlQry = new StringBuilder("SELECT u FROM Users u WHERE u.employeeId=:EMPLOYEEID");
			Query queryObj = firstEntityManager.createQuery(sqlQry.toString());
			queryObj.setParameter(AppicationConstants.EMPLOYEEID_STR, CommonUtil.nullRemove(employeeId));
			usersList =  queryObj.getResultList();
			if(usersList != null && !usersList.isEmpty()) {
				Users users = usersList.get(0);
				if(!CommonUtil.nullRemove(status).isEmpty()) {
					users.setStatus(status);
				}
				if(!CommonUtil.nullRemove(updatedBy).isEmpty()) {
					users.setUpdatedBy(updatedBy);
				}
				firstEntityManager.merge(users);
				result = true;
			}
		} catch (Exception e) {
			logger.info("Exception :: UserDAOImpl :: updateUserStatusByEmployeeId() : " + e);
		} finally {
			firstEntityManager.close();
		}
		
		return result;
	}

	@Override
	@Transactional(value = "firstTransactionManager", propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public boolean UpdateUser(UserDto userDto) throws Exception {
		StringBuilder sqlQry = null;
		boolean result = false;
		try {
			Users user = firstEntityManager.find(Users.class, userDto.getAutogenUsersId());
			if (user != null) {
				if (!CommonUtil.nullRemove(userDto.getFirstName()).isEmpty()) {
					user.setEmail(userDto.getFirstName());
				}
				if (!CommonUtil.nullRemove(userDto.getLastName()).isEmpty()) {
					user.setLastName(userDto.getLastName());
				}
				if (!CommonUtil.nullRemove(userDto.getEmail()).isEmpty()) {
					user.setEmail(userDto.getEmail());
				}
				if (!CommonUtil.nullRemove(userDto.getMobileNumber()).isEmpty()) {
					user.setMobileNumber(userDto.getMobileNumber());
				}

				if (!CommonUtil.nullRemove(userDto.getStatus()).isEmpty()) {
					user.setStatus(userDto.getStatus());
				}

				firstEntityManager.merge(user);

				List<UsersDetail> subResultObj = null;
				sqlQry = new StringBuilder("SELECT usd FROM UsersDetail usd where usd.autogenUsersId=:USERID");
				Query subQueryObj = firstEntityManager.createQuery(sqlQry.toString());
				subQueryObj.setParameter("USERID", user.getAutogenUsersId());
				subResultObj = subQueryObj.getResultList();
				if(subResultObj != null) {
				for (UsersDetail usersDetail : subResultObj) {
					if (userDto.getAutogenRolesId() != null && userDto.getRolesName() != null) {
						usersDetail.setAutogenRolesId(userDto.getAutogenRolesId());
						usersDetail.setRolesName(userDto.getRolesName());
					}

					if (userDto.getSupervisorUsersId() != null && userDto.getSupervisorUsersName() != null) {
						usersDetail.setSupervisorUsersId(userDto.getSupervisorUsersId());
						usersDetail.setSupervisorUsersName(userDto.getSupervisorUsersName());
					}

					if (userDto.getUpdatedBy() != null) {
						usersDetail.setUpdatedBy(userDto.getUpdatedBy());
					}
					firstEntityManager.merge(usersDetail);
					if (usersDetail.getAutogenUsersDetailsId() != null) {
                      List<String> existinginventoryIds = null;
						if (userDto.getUserInventoryMapDtoList() != null) {
							Query qryObj = null;
							qryObj = firstEntityManager.createQuery(
									"SELECT i FROM UserInventoryMap i WHERE autogenUsersDetailsId=:USERDETAILSID");
							qryObj.setParameter("USERDETAILSID", usersDetail.getAutogenUsersDetailsId());
							List<UserInventoryMap> userInventoryMapResult = qryObj.getResultList();

							if (userInventoryMapResult != null) {
								qryObj = firstEntityManager.createQuery(
										"UPDATE UserInventoryMap SET status=:STATUS WHERE autogenUsersDetailsId=:USERDETAILSID");
								qryObj.setParameter("USERDETAILSID", usersDetail.getAutogenUsersDetailsId());
								qryObj.setParameter(AppicationConstants.STATUS_STR, AppicationConstants.INACTIVE_STR);
								qryObj.executeUpdate();
								
								List<UserInventoryMapDto> removeUserInventoryMapDtoList = new ArrayList<>();
								List<UserInventoryMapDto> newUserInventoryMapDtoList = userDto.getUserInventoryMapDtoList();
								/**  Old UserInventoryMap Status Update  */
								existinginventoryIds = new ArrayList<>();
								for (UserInventoryMapDto userInventoryMapDto : newUserInventoryMapDtoList) {
									for(UserInventoryMap p :userInventoryMapResult) {
										if (p.getInventoryRegionId().equals(userInventoryMapDto.getInventoryRegionId())
												&& p.getInventoryCenterId().equals(userInventoryMapDto
														.getInventoryCenterId())
												&& p.getInventoryClientId().equals(userInventoryMapDto
														.getInventoryClientId())
												&& p.getInventoryProcessId().equals(userInventoryMapDto
														.getInventoryProcessId())) {
											existinginventoryIds.add(String.valueOf(p.getAutogenUserInventoryMapId()));
											removeUserInventoryMapDtoList.add(userInventoryMapDto);
										}
										}
								}
                                if(!existinginventoryIds.isEmpty()) {
                                  existinginventoryIds.forEach(mapId->{
                                    Query qryObjTemp = firstEntityManager.createNativeQuery(
                                        "UPDATE user_inventory_map SET status=:STATUS WHERE AUTOGEN_USER_INVENTORY_MAP_ID=:INVENTORYMAPID");
                                    qryObjTemp.setParameter("INVENTORYMAPID", mapId);
                                    qryObjTemp.setParameter(AppicationConstants.STATUS_STR, AppicationConstants.ACTIVE_STR);
                                    qryObjTemp.executeUpdate();
                                  });
                               
                                }
								newUserInventoryMapDtoList.removeAll(removeUserInventoryMapDtoList);
								for (UserInventoryMapDto userInventoryMapDto : newUserInventoryMapDtoList) {

									UserInventoryMap userInventoryMap = new UserInventoryMap();
									userInventoryMap.setAutogenUsersDetailsId(
											usersDetail.getAutogenUsersDetailsId());
									userInventoryMap.setInventoryRegionId(
											userInventoryMapDto.getInventoryRegionId());
									userInventoryMap.setInventoryRegionName(
											userInventoryMapDto.getInventoryRegionName());
									userInventoryMap.setInventoryCenterId(
											userInventoryMapDto.getInventoryCenterId());
									userInventoryMap.setInventoryCenterName(
											userInventoryMapDto.getInventoryCenterName());
									userInventoryMap.setInventoryClientId(
											userInventoryMapDto.getInventoryClientId());
									userInventoryMap.setInventoryClientName(
											userInventoryMapDto.getInventoryClientName());
									userInventoryMap.setInventoryProcessId(
											userInventoryMapDto.getInventoryProcessId());
									userInventoryMap.setInventoryProcessName(
											userInventoryMapDto.getInventoryProcessName());
									userInventoryMap.setInventoryCategoryId(
											userInventoryMapDto.getInventoryCategoryId());
									userInventoryMap.setInventoryCategoryName(
											userInventoryMapDto.getInventoryCategoryName());
									userInventoryMap.setStatus(AppicationConstants.ACTIVE_STR);
									userInventoryMap.setCreatedBy(userDto.getCreatedBy());
									firstEntityManager.persist(userInventoryMap);
								
								}
							}
						}
						
						if (userDto.getReports() != null) {
							Query qryObj = null;
							qryObj = firstEntityManager.createQuery(
									"SELECT i FROM UserReportMap i WHERE autogenUsersDetailsId=:USERDETAILSID");
							qryObj.setParameter("USERDETAILSID", usersDetail.getAutogenUsersDetailsId());
							List<UserReportMap> userReportMapResult = qryObj.getResultList();

							if (userReportMapResult != null) {
								qryObj = firstEntityManager.createQuery(
										"UPDATE UserReportMap SET status=:STATUS WHERE autogenUsersDetailsId=:USERDETAILSID");
								qryObj.setParameter("USERDETAILSID", usersDetail.getAutogenUsersDetailsId());
								qryObj.setParameter(AppicationConstants.STATUS_STR, AppicationConstants.INACTIVE_STR);
								qryObj.executeUpdate();
								List<Reports> newReports =  userDto.getReports();
								List<Reports> removeReports =  new ArrayList<>();
								/**  Old Reports Status Update  */
								existinginventoryIds = new ArrayList<>();
								for (Reports reports : userDto.getReports()) {
									try {
										for(UserReportMap p :userReportMapResult){
											if (p.getAutogenReportMasterId().equals(reports.getId())) {
											    existinginventoryIds.add(String.valueOf(p.getAutogenUserReportMapId()));
												removeReports.add(reports);
											}
										}
									} catch (Exception e) {
										logger.info(
												"Exception :: UserDAOImpl :: UpdateUser() Nested Try Exception : " + e);
									}
								}
					         if(!existinginventoryIds.isEmpty()) {
	                                  existinginventoryIds.forEach(mapId->{
	                                    Query qryObjTemp = firstEntityManager.createNativeQuery(
	                                        "UPDATE user_report_map SET status=:STATUS WHERE AUTOGEN_USER_REPORT_MAP_ID=:INVENTORYMAPID");
	                                    qryObjTemp.setParameter("INVENTORYMAPID", mapId);
	                                    qryObjTemp.setParameter(AppicationConstants.STATUS_STR, AppicationConstants.ACTIVE_STR);
	                                    qryObjTemp.executeUpdate();
	                                  });
	                               
	                            }
								
								newReports.removeAll(removeReports);
								/**  New Reports Insert  */
								for (Reports reports : newReports) {
									UserReportMap userReportMap = new UserReportMap();
									userReportMap.setAutogenUsersDetailsId(
											usersDetail.getAutogenUsersDetailsId());
									userReportMap.setAutogenReportMasterId(reports.getId());
									userReportMap.setReportName(reports.getReportName());
									userReportMap.setStatus(AppicationConstants.ACTIVE_STR);
									userReportMap.setCreatedBy(userDto.getCreatedBy());
									firstEntityManager.persist(userReportMap);
								}
							}
						}
						
						if (userDto.getUserLeaveDetailsDtoList() != null) {
								for (UserLeaveDetailsDto userLeaveDetailsDto : userDto.getUserLeaveDetailsDtoList()) {
										if(userLeaveDetailsDto.getLeaveDetailsId() != null && !BigInteger.ZERO.equals(userLeaveDetailsDto.getLeaveDetailsId()) &&
												(AppicationConstants.ACTIVE_STR.equalsIgnoreCase(userLeaveDetailsDto.getStatus())||"CANCEL".equalsIgnoreCase(userLeaveDetailsDto.getStatus()))) {
											UserLeaveDetails userLeaveDetails = firstEntityManager.find(UserLeaveDetails.class, userLeaveDetailsDto.getLeaveDetailsId());
											if (userLeaveDetails != null) {
												userLeaveDetails.setToDate(new Date());
												userLeaveDetails.setFromDate(new Date());
												userLeaveDetails.setReasons(userLeaveDetailsDto.getReasons());
												userLeaveDetails.setComments(userLeaveDetailsDto.getComments());
												userLeaveDetails.setStatus(userLeaveDetailsDto.getStatus());
												userLeaveDetails.setUpdatedBy(userDto.getUpdatedBy());
												firstEntityManager.merge(userLeaveDetails);
											} 
										
										} else {
											UserLeaveDetails userLeaveDetails = new UserLeaveDetails();
											userLeaveDetails.setAutogenUsersDetailsId(usersDetail.getAutogenUsersDetailsId());
											if(userLeaveDetailsDto.getFromDate() != null && !userLeaveDetailsDto.getFromDate().isEmpty()) {
											userLeaveDetails.setFromDate(DateUtil.convertStringtoDate(userLeaveDetailsDto.getFromDate(), DateUtil.DATE_MONTH_YEAR_SLASH_PATTERN));
											}
											if(userLeaveDetailsDto.getToDate() != null && !userLeaveDetailsDto.getToDate().isEmpty()) {
											userLeaveDetails.setToDate(DateUtil.convertStringtoDate(userLeaveDetailsDto.getToDate(), DateUtil.DATE_MONTH_YEAR_SLASH_PATTERN));
											}userLeaveDetails.setNoOfDays(userLeaveDetailsDto.getNoOfDays());
											userLeaveDetails.setReasons(userLeaveDetailsDto.getReasons());
											userLeaveDetails.setComments(userLeaveDetailsDto.getComments());
											userLeaveDetails.setStatus(userLeaveDetailsDto.getStatus());
											userLeaveDetails.setCreatedBy(userDto.getUpdatedBy());
											firstEntityManager.persist(userLeaveDetails);
										}
								}
							}
						}
				}
			}
				result = true;
			}
		} catch (Exception e) {
			logger.info("Exception :: UserDAOImpl :: UpdateUser() : " + e);
		} finally {
			firstEntityManager.close();
		}

		return result;
	}
	
	/*public AgentDto getAgentDetList(AgentDto agentDto) throws Exception {
		StringBuilder sqlQry = null;
		List<Object[]> newAgentList = new ArrayList<>(); 
		
		try {
			String agentIds = "";
			if (agentDto.getResultObj() != null) {
				List<String> agentList = (List<String>) agentDto.getResultObj();
				for (String agentId : agentList) {
					agentIds = agentIds + "'" + CommonUtil.nullRemove(agentId.trim()) + "'";
					if (!(agentList.size() - 1 == agentList.indexOf(agentId))) {
						agentIds = agentIds + ", ";
					}
				}
			}
			List<Object[]> resultObjList = new ArrayList<>();
			sqlQry = new StringBuilder(
					"SELECT AM.AGENT_ID, AM.AGENT_NAME, AM.AUDITOR_ID,AM.AUDITOR_NAME, USD.AUTOGEN_USERS_DETAILS_ID FROM USERS US, USERS_DETAILS USD, AGENT_MAPPING AM WHERE AM.AGENT_ID=US.EMPLOYEE_ID AND US.AUTOGEN_USERS_ID=USD.AUTOGEN_USERS_ID AND US.STATUS IN ('Active','NEW') AND TRIM(US.EMPLOYEE_ID) IN("
							+ agentIds + ")");
			Query queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
			resultObjList = queryObj.getResultList();
			if(resultObjList != null && !resultObjList.isEmpty()) {
				for(Object[] agentObj :resultObjList) {
					Object[] objArray = new Object[2];
					objArray[0] = agentObj;
					sqlQry = new StringBuilder("SELECT UIM.INVENTORY_REGION_ID, UIM.INVENTORY_REGION_NAME, UIM.INVENTORY_CENTER_ID, UIM.INVENTORY_CENTER_NAME, UIM.INVENTORY_CLIENT_ID, UIM.INVENTORY_CLIENT_NAME, UIM.INVENTORY_PROCESS_ID, UIM.INVENTORY_PROCESS_NAME, UIM.INVENTORY_CATEGORY_ID, UIM.INVENTORY_CATEGORY_NAME FROM USER_INVENTORY_MAP UIM WHERE UIM.AUTOGEN_USERS_DETAILS_ID =:AGENTID");
					queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
					queryObj.setParameter("AGENTID", CommonUtil.nullRemove(agentObj[4]));
					List<Object[]> agentCategoryObjList = queryObj.getResultList();
					objArray[1] = agentCategoryObjList;
					newAgentList.add(objArray);
				}				
			}
			agentDto.setResultObjList(newAgentList);
		} catch (Exception e) {
			logger.info("Exception :: UserDAOImpl :: getAgentDetList() : " + e);
		} finally {
			firstEntityManager.close();
		}

		return agentDto;
	}*/
	
	
	public UserDto getSuperVisorUsersList() throws Exception {
		StringBuilder sqlQry = null;
		List<Object[]> users = null;
		UserDto userDto = null;
		try {
			sqlQry = new StringBuilder("SELECT US.EMPLOYEE_ID, concat(US.FIRST_NAME ,' ',US.LAST_NAME) AS NAME FROM USERS US WHERE US.STATUS='Active'");
			Query queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
			users = queryObj.getResultList();
			if(users != null && !users.isEmpty()) {
				userDto = new UserDto();
				userDto.setResultObjList(users);
			}
		} catch (Exception e) {
			logger.info("Exception :: UserDAOImpl :: getSuperVisorUsersList() : " + e);
		} finally {
			firstEntityManager.close();
		}

		return userDto;
	}

	/*@Override
	public AgentDto getAgentList(AgentDto agentDto) throws Exception {
		StringBuilder sqlQry = null;
		List<Object[]> mappedAgentList = null;
		StringBuilder employeeIds = new StringBuilder();
		String regionIds = AppicationConstants.EMPTY_STR;
		String centerIds = AppicationConstants.EMPTY_STR;
		String clientIds = AppicationConstants.EMPTY_STR;
		String processesIds = AppicationConstants.EMPTY_STR;
		try {
			sqlQry = new StringBuilder("SELECT DISTINCT AGENT_ID AS AGENTID, AGENT_NAME AS NAME, AUTOGEN_AGENT_MAPPING_ID AS AGENTMAPID, STATUS FROM AGENT_MAPPING WHERE STATUS='Active' AND AUDITOR_ID=:AUDITORID ORDER BY NAME");
			Query queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
			queryObj.setParameter("AUDITORID", agentDto.getAuditorId());
			mappedAgentList = queryObj.getResultList();

			if(mappedAgentList != null && !mappedAgentList.isEmpty()) {
				for (Object[] obj : mappedAgentList) {
					employeeIds.append(employeeIds + "'"+CommonUtil.nullRemove(obj[0])+"'");
					boolean conditionFlag = (mappedAgentList.size()-1 == mappedAgentList.indexOf(obj));
					if(!conditionFlag) {
						employeeIds.append(employeeIds+", ");
					}
				}
			}

			if (agentDto.getRegions() != null) {
				regionIds = StringUtils.arrayToCommaDelimitedString(agentDto.getRegions().toArray());
			}
			if (agentDto.getCenters() != null) {
				centerIds = StringUtils.arrayToCommaDelimitedString(agentDto.getCenters().toArray());
			}
			if (agentDto.getClients() != null) {
				clientIds = StringUtils.arrayToCommaDelimitedString(agentDto.getClients().toArray());
			}
			if (agentDto.getProcesses() != null) {
				processesIds = StringUtils.arrayToCommaDelimitedString(agentDto.getProcesses().toArray());
			}
			
			sqlQry = new StringBuilder("SELECT DISTINCT U.EMPLOYEE_ID AS AGENTID, CONCAT(U.FIRST_NAME,' ',U.LAST_NAME) AS NAME, '' AS AGENTMAPID, '' AS STATUS  FROM USERS U, USERS_DETAILS UD, USER_INVENTORY_MAP UIM "
					 +" WHERE  UD.AUTOGEN_ROLES_ID=(SELECT AUTOGEN_ROLES_ID FROM ROLES WHERE ROLES_NAME=:ROLESNAME) AND UD.AUTOGEN_USERS_ID=U.AUTOGEN_USERS_ID "
					 +" AND UIM.INVENTORY_REGION_ID IN(:REGIONID) AND UIM.INVENTORY_CENTER_ID IN (:CENTERID) AND UIM.INVENTORY_CLIENT_ID IN (:CLIENTID) AND UIM.INVENTORY_PROCESS_ID IN(:PROCESSID)"
					 +" AND UD.AUTOGEN_USERS_DETAILS_ID=UIM.AUTOGEN_USERS_DETAILS_ID " 
					 +" AND "
					 +" U.EMPLOYEE_ID NOT IN (:EMPLOYEEIDS) OR NULL AND UIM.STATUS='Active' AND U.STATUS IN('Active','NEW')");
			queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
			queryObj.setParameter(AppicationConstants.ROLESNAME_STR, "Agent");
			queryObj.setParameter(AppicationConstants.REGIONID_STR, regionIds);
			queryObj.setParameter("CENTERID", centerIds);
			queryObj.setParameter("CLIENTID", clientIds);
			queryObj.setParameter("PROCESSID", processesIds);
			queryObj.setParameter("EMPLOYEEIDS", employeeIds);
			List<Object[]> agentList = queryObj.getResultList();
			if(agentList != null && !agentList.isEmpty()) {
				mappedAgentList = new ArrayList<>();
				mappedAgentList.addAll(agentList);
			}
		} catch (Exception e) {
			logger.info("Exception :: UserDAOImpl :: getAgentList() : " + e);
		} finally {
			firstEntityManager.close();
		}
		agentDto.setResultObjList(mappedAgentList);
		return agentDto;
	}*/

	public UserDto getModuleScreenDet(String rolesName) throws Exception {
		StringBuilder sqlQry = null;
		List<Object[]> screenList = new ArrayList<>();
		UserDto userDto = null;
		try {
			sqlQry = new StringBuilder("SELECT MODULE_UID, MODULE_NAME, SCREEN_UID, SCREEN_NAME, ACCESS_PERMISSION FROM USER_SCREEN_MAP WHERE STATUS='Active' AND ROLES_NAME=:ROLESNAME GROUP BY 1,3;");
			Query queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
			queryObj.setParameter(AppicationConstants.ROLESNAME_STR, rolesName);
			screenList = queryObj.getResultList();
			if(screenList != null && !screenList.isEmpty()) {
				userDto = new UserDto();
				userDto.setResultObjList(screenList);
			}
		} catch (Exception e) {
			logger.info("Exception :: UserDAOImpl :: getModuleScreenDet() : " + e);
		} finally {
			firstEntityManager.close();
		}

		return userDto;
	}

/*	@Override
	public AgentDto getRoleAndInventoryByUsersList(AgentDto agentDto, String roleName) throws Exception {
		StringBuilder sqlQry = null;
		Query queryObj  = null;
		List<Object[]> usersMapList = null;
		String regionIds = AppicationConstants.EMPTY_STR;
		String centerIds = AppicationConstants.EMPTY_STR;
		String clientIds = AppicationConstants.EMPTY_STR;
		String processesIds = AppicationConstants.EMPTY_STR;
		try {

			if (agentDto.getRegions() != null) {
				regionIds = StringUtils.arrayToCommaDelimitedString(agentDto.getRegions().toArray());
			}
			if (agentDto.getCenters() != null) {
				centerIds = StringUtils.arrayToCommaDelimitedString(agentDto.getCenters().toArray());
			}
			if (agentDto.getClients() != null) {
				clientIds = StringUtils.arrayToCommaDelimitedString(agentDto.getClients().toArray());
			}
			if (agentDto.getProcesses() != null) {
				processesIds = StringUtils.arrayToCommaDelimitedString(agentDto.getProcesses().toArray());
			}
			
			sqlQry = new StringBuilder("SELECT DISTINCT U.EMPLOYEE_ID AS USERID, CONCAT(U.FIRST_NAME,' ',U.LAST_NAME) AS NAME FROM USERS U, USERS_DETAILS UD, USER_INVENTORY_MAP UIM "
					 +" WHERE  UD.AUTOGEN_ROLES_ID=(SELECT AUTOGEN_ROLES_ID FROM ROLES WHERE ROLES_NAME=:ROLESNAME) AND UD.AUTOGEN_USERS_ID=U.AUTOGEN_USERS_ID "
					 +" AND UIM.INVENTORY_REGION_ID IN(:REGIONID) AND UIM.INVENTORY_CENTER_ID IN (:CENTERID) AND UIM.INVENTORY_CLIENT_ID IN (:CLIENTID) AND UIM.INVENTORY_PROCESS_ID IN(:PROCESSID)"
					 +" AND UD.AUTOGEN_USERS_DETAILS_ID=UIM.AUTOGEN_USERS_DETAILS_ID AND " 
					 +" UIM.STATUS='Active' AND U.STATUS IN('Active','NEW')");
			queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
			queryObj.setParameter(AppicationConstants.ROLESNAME_STR, roleName);
			queryObj.setParameter(AppicationConstants.REGIONID_STR, regionIds);
			queryObj.setParameter(AppicationConstants.CENTERID_STR, centerIds);
			queryObj.setParameter(AppicationConstants.CLIENTID_STR, clientIds);
			queryObj.setParameter(AppicationConstants.PROCESSID_STR, processesIds);
			List<Object[]> usersist = queryObj.getResultList();
			if(usersist != null && !usersist.isEmpty()) {
				usersMapList = new ArrayList<>();
				usersMapList.addAll(usersist);
			}
		} catch (Exception e) {
			logger.info("Exception :: UserDAOImpl :: getRoleAndInventoryByUsersList() : " + e);
		} finally {
			firstEntityManager.close();
		}
		agentDto.setResultObjList(usersMapList);
		return agentDto;
	}*/
	
	@Transactional(value = "firstTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean resetPassword(UserDto userDto) throws Exception {
		List<Users> usersList = null;
		boolean result = false;
		try {
			StringBuilder sqlQry = new StringBuilder("SELECT u FROM Users u WHERE u.employeeId=:EMPLOYEEID");
			Query queryObj = firstEntityManager.createQuery(sqlQry.toString());
			queryObj.setParameter(AppicationConstants.EMPLOYEEID_STR, CommonUtil.nullRemove(userDto.getEmployeeId()));
			usersList =  queryObj.getResultList();
			if(usersList != null && !usersList.isEmpty()) {
				Users users = usersList.get(0);
				if(!CommonUtil.nullRemove(userDto.getStatus()).isEmpty()) {
					users.setStatus(userDto.getStatus());
				}
				if(!CommonUtil.nullRemove(userDto.getEmployeeId()).isEmpty()) {
					users.setUpdatedBy(userDto.getEmployeeId());
				}
				if(!CommonUtil.nullRemove(userDto.getPassword()).isEmpty()) {
					users.setPassword(userDto.getPassword());
				}
				firstEntityManager.merge(users);
				result = true;
			}
		} catch (Exception e) {
			logger.info("Exception :: UserDAOImpl :: resetPassword() : " + e);
		} finally {
			firstEntityManager.close();
		}
		
		return result;
	}

	@SuppressWarnings("unchecked")
	public boolean checkExistingTokenDetails(TokenDetailsDto tokenDetailsDto) throws Exception {

		StringBuilder sqlQry = null;
		List<Object[]> tokenDet = new ArrayList<>();
		boolean result = false;
		try {
			sqlQry = new StringBuilder(
					"select max(autogen_token_details_id) from ratesheet_token_details where EMPLOYEE_ID=:EMPLOYEEID and token=:TOKEN and STATUS='Active';");
			Query queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
			queryObj.setParameter(AppicationConstants.EMPLOYEEID_STR, tokenDetailsDto.getEmployeeId());
			queryObj.setParameter(AppicationConstants.TOKEN_STR, tokenDetailsDto.getToken());
			tokenDet = queryObj.getResultList();
			if (tokenDet != null && !tokenDet.isEmpty() && tokenDet.get(0) != null) {
				result = true;
			}
		} catch (Exception e) {
			logger.info("Exception :: UserDAOImpl :: checkExistingTokenDetails() : " + e);
		} finally {
			firstEntityManager.close();
		}

		return result;
	}

	@Transactional(value = "firstTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public TokenDetailsDto saveTokenDetails(TokenDetailsDto tokenDetailsDto) throws Exception {
		try {
			if(tokenDetailsDto != null) {
			  Query updateQry = firstEntityManager.createNativeQuery("UPDATE ratesheet_token_details SET STATUS='INACTIVE' WHERE status='ACTIVE' AND EMPLOYEE_ID=:EMPID");
              updateQry.setParameter("EMPID", tokenDetailsDto.getEmployeeId());
              updateQry.executeUpdate();
				tokenDetailsDto.setFlag(false);
				TokenDetails tokenDetails = new TokenDetails();
				BeanUtils.copyProperties(tokenDetailsDto, tokenDetails);
				firstEntityManager.persist(tokenDetails);
				tokenDetailsDto.setFlag(true);
			}
		} catch (Exception e) {
			logger.info("Exception :: UserDAOImpl :: saveTokenDetails() : " + e);
		} finally {
			firstEntityManager.close();
		}
		
		return tokenDetailsDto;
	}

	@Transactional(value = "firstTransactionManager", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public TokenDetailsDto updateTokenStatus(TokenDetailsDto tokenDetailsDto) throws Exception {
		List<TokenDetails> tokenList = null;
		try {
			StringBuilder sqlQry = new StringBuilder("SELECT u FROM TokenDetails u WHERE u.employeeId=:EMPLOYEEID and token=:TOKEN");
			Query queryObj = firstEntityManager.createQuery(sqlQry.toString());
			queryObj.setParameter(AppicationConstants.EMPLOYEEID_STR, tokenDetailsDto.getEmployeeId());
			queryObj.setParameter(AppicationConstants.TOKEN_STR, tokenDetailsDto.getToken());
			tokenList =  queryObj.getResultList();
			if(tokenList != null && !tokenList.isEmpty()) {
				TokenDetails tokenDetails = tokenList.get(0);
				tokenDetails.setStatus(AppicationConstants.COMPLETE_STR);
				tokenDetails.setUpdatedBy(tokenDetailsDto.getEmployeeId());
				firstEntityManager.persist(tokenDetails);
				tokenDetailsDto.setFlag(true);
			}
		} catch (Exception e) {
			logger.info("Exception :: UserDAOImpl :: updateTokenDetails() : " + e);
		} finally {
			firstEntityManager.close();
		}
		
		return tokenDetailsDto;
	}

	public TokenDetailsDto fetchTokenDetails(TokenDetailsDto tokenDetailsDto) throws Exception {
		return tokenDetailsDto;
	}

	@Override
	public UserDto getUserReportList(UserDto userDto) throws Exception {
		List<Object[]> userReportList = null;
		try {
			Query queryObj = firstEntityManager.createQuery("SELECT urm.autogenReportMasterId, urm.reportName, urm.status from UserReportMap urm, UsersDetail ud where ud.autogenUsersDetailsId=urm.autogenUsersDetailsId and ud.autogenUsersId=:USERSID and urm.status='Active'");
			queryObj.setParameter("USERSID", userDto.getAutogenUsersId());
			userReportList = queryObj.getResultList();
		} catch(Exception e) {
			logger.info("Exception :: UserDAOImpl :: getUserReportList() : " + e);
		} finally {
			firstEntityManager.close();
		}
		userDto.setResultObj(userReportList);
		return userDto;
	}

}
