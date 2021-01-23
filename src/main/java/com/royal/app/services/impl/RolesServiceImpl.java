package com.royal.app.services.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import com.royal.app.dao.RolesDAO;
import com.royal.app.message.response.RolesResponse;
import com.royal.app.model.Roles;
import com.royal.app.model.Users;
import com.royal.app.services.RolesService;
import com.royal.app.shared.dto.UserDto;

@Service
public class RolesServiceImpl implements RolesService {

	@Autowired
	RolesDAO rolesDAO;
	
	@Override
	public Optional<Roles> findByName(String roleName) {
		return rolesDAO.findByName(roleName);
	}

	@Override
	public List<Object[]> getUserRoles(int userId) {
		return rolesDAO.getUserRoles(userId);
	}
	
	public List<GrantedAuthority> getGrantedAuthorities(Users users, List<Object[]> roles){
		List<GrantedAuthority> authorities = new ArrayList<>();
		
		for(Object[] role : roles){
			authorities.add(new SimpleGrantedAuthority(Arrays.toString(role)));
		}
		return authorities;
	}

	@Override
	public UserDto getRoles() throws Exception {
		UserDto userDto = null;
		List<Object[]> rolesObjList =  rolesDAO.getRoles();
		if(rolesObjList != null && !rolesObjList.isEmpty()) {
			List<RolesResponse> rolesResList = new ArrayList<>();
			rolesObjList.stream().forEach(obj ->{
				rolesResList.add(new RolesResponse(new BigInteger((String)obj[0]), String.valueOf(obj[1])));
			});
			userDto = new UserDto();
			userDto.setResultObj(rolesResList);
		}
		return userDto;
	}
	
	public Map<?, ?> getRolesByKeyValuePair() throws Exception {
		Map<String, BigInteger> resultMap = new HashMap<>();
		List<Object[]> rolesObjList = rolesDAO.getRoles();
		if (rolesObjList != null && !rolesObjList.isEmpty()) {
			rolesObjList.stream().forEach(obj -> {
				resultMap.put(String.valueOf(obj[1]), new BigInteger((String) obj[0]));
			});
		}
		return resultMap;
	}

}
