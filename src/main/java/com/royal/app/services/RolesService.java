package com.royal.app.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import com.royal.app.model.Roles;
import com.royal.app.model.Users;
import com.royal.app.shared.dto.UserDto;

public interface RolesService {
	
	Optional<Roles> findByName(String roleName);
	
	public List<Object[]> getUserRoles(int userId);
	
	public List<GrantedAuthority> getGrantedAuthorities(Users users, List<Object[]> roles);
	
	public UserDto getRoles() throws Exception; 
	
	public Map<?, ?> getRolesByKeyValuePair() throws Exception;

}
