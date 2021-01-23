package com.royal.app.dao.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.royal.app.constants.AppicationConstants;
import com.royal.app.dao.RolesDAO;
import com.royal.app.model.Roles;
import com.royal.app.shared.dto.UserDto;

@Repository
public class RolesDAOImpl implements RolesDAO {

	@PersistenceContext(unitName = AppicationConstants.FIRST_PERSISTENCE_UNIT_NAME)
	public EntityManager firstEntityManager;

	@Autowired
	@Qualifier(AppicationConstants.FIRST_JDBC_TEMPLATE)
	JdbcTemplate firstJdbcTemplate;

	private Logger logger = Logger.getLogger(RolesDAOImpl.class);

	
	@SuppressWarnings("unchecked")
	@Override
	public Optional<Roles> findByName(String roleName) {
		StringBuilder sqlQry = null;
		Optional<Roles> rolesOptional = null;
		List<Roles> rolesList = null;
		String rolename = "";
		try {
			sqlQry = new StringBuilder("SELECT rs FROM Roles rs WHERE rs.roleName=:ROLENAME");
			Query queryObj = firstEntityManager.createQuery(sqlQry.toString());
			queryObj.setParameter("ROLENAME", roleName);
			rolesList =   (List<Roles>) queryObj.getResultList();
			
			if(!rolesList.isEmpty()) {
				for (Roles object : rolesList) {
					rolesOptional = Optional.ofNullable(object);/**
					Roles roles = new Roles();
					roles.setAutogenRolesId(String.valueOf(object[0]));
					rolename = String.valueOf(object[1]);
					roles.setRoleName(rolename);
					if(RoleName.ROLE_ADMIN.toString().equalsIgnoreCase(rolename)) {
						roles.setName(RoleName.ROLE_ADMIN);
					} else if(RoleName.ROLE_PM.toString().equalsIgnoreCase(rolename)) {
						roles.setName(RoleName.ROLE_PM);
					} else if(RoleName.ROLE_USER.toString().equalsIgnoreCase(rolename)) {
						roles.setName(RoleName.ROLE_USER);
					}*/
				
				}
			}
			
			
		} catch (Exception e) {
			logger.info("Error :: DemoDAOImpl :: findByName() : " + e);
		} finally {
			firstEntityManager.close();
		}
		return rolesOptional;
	}
	
	public List<Object[]> getUserRoles(int userId) {
		StringBuilder sqlQry = null;
		List<Object[]> rolesList = null;
		String rolename = "";
		try {
			sqlQry = new StringBuilder("SELECT NAME FROM ROLES WHERE AUTOGEN_ROLES_ID IN(SELECT AUTOGEN_ROLES_ID FROM USER_ROLES_MAP WHERE AUTOGEN_USERS_ID=:USERID)");
			Query queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
			queryObj.setParameter("USERID", userId);
			rolesList =   (List<Object[]>) queryObj.getResultList();
		} catch (Exception e) {
			logger.info("Error :: RolesDAOImpl :: getUserRoles() : " + e);
		} finally {
			firstEntityManager.close();
		}
		return rolesList;
	}
	
	public List<Object[]> getUserGroupRoles(int userId) {
		StringBuilder sqlQry = null;
		List<Object[]> rolesList = null;
		try {
			sqlQry = new StringBuilder("SELECT NAME FROM GROUP_ROLE WHERE AUTOGEN_USER_GROUP_ID IN(SELECT AUTOGEN_USER_GROUP_ID FROM USER_GROUP_ROLES WHERE AUTOGEN_USER_GROUP_ID=:USERID)");
			Query queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
			queryObj.setParameter("USERID", userId);
			rolesList =   (List<Object[]>) queryObj.getResultList();
		} catch (Exception e) {
			logger.info("Error :: RolesDAOImpl :: getUserRoles() : " + e);
		} finally {
			firstEntityManager.close();
		}
		return rolesList;
	}
	
	public String getRoleById(BigInteger roleId) throws Exception{
		StringBuilder sqlQry = null;
		List<Object> rolesList = null;
		String roleName = "";
		try {
			sqlQry = new StringBuilder("SELECT ROLE_NAME FROM ROLES WHERE AUTOGEN_ROLES_ID=:ROLEID");
			Query queryObj = firstEntityManager.createNativeQuery(sqlQry.toString());
			queryObj.setParameter("ROLEID", roleId);
			rolesList =   (List<Object>) queryObj.getResultList();
			
			if(rolesList != null && !rolesList.isEmpty()) {
				roleName = (String) rolesList.get(0);
			}
		} catch (Exception e) {
			logger.info("Error :: RolesDAOImpl :: getRoleById() : " + e);
		} finally {
			firstEntityManager.close();
		}
		return roleName;
	}

	public List<Object[]> getRoles() throws Exception {
		StringBuilder sqlQry = null;
		List<Object[]> rolesList = new ArrayList<>();
		try {
			sqlQry = new StringBuilder(
					"SELECT rs.autogenRolesId, rs.rolesName FROM Roles rs WHERE rs.status='ACTIVE'");
			Query queryObj = firstEntityManager.createQuery(sqlQry.toString());
			rolesList = queryObj.getResultList();
		} catch (Exception e) {
			logger.info("Exception :: UserDAOImpl :: getCategoryByEmployeId() : " + e);
		} finally {
			firstEntityManager.close();
		}
		
		return rolesList;
	}

	
	

}
