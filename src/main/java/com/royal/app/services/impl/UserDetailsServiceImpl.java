package com.royal.app.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.royal.app.constants.AppicationConstants;
import com.royal.app.dao.RolesDAO;
import com.royal.app.dao.UserDAO;
import com.royal.app.model.Roles;
import com.royal.app.shared.dto.UserDto;
 
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
 
    @Autowired
    UserDAO userDAO;
    
    @Autowired
    RolesDAO rolesDAO;
    
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
 
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
    	 UserDto userDto = null;
		try {
			userDto = userDAO.findByUsername(username).orElseThrow(() -> 
			                new UsernameNotFoundException(AppicationConstants.USERNAMENOTFOUNDEXCEPTION)
			);
		} catch (Exception e) {
			logger.error("Exception::UserDetailsServiceImpl.Class:loadUserByUsername()", e);
		}
		
		if(userDto == null || userDto.getAutogenUsersId() == null) {
			throw new UsernameNotFoundException(AppicationConstants.USERNAMENOTFOUNDEXCEPTION);
		}
		if("Blocked".equalsIgnoreCase(userDto.getStatus())) {
			throw new LockedException("Your account is blocked. Please contact the system administrator.");
		} else if("INACTIVE".equalsIgnoreCase(userDto.getStatus())) {
			throw new DisabledException("Your account is inactive. Please contact the system administrator."); 
		}/*else if("New".equalsIgnoreCase(userDto.getStatus())) {
			UserDto userDtoUpdate = new UserDto();
			userDtoUpdate.setAutogenUsersId(userDto.getAutogenUsersId());
			userDtoUpdate.setStatus("Active");
			userDtoUpdate.setUpdatedBy(userDto.getEmployeeId());
			try {
				userDAO.UpdateUserStatus(userDtoUpdate);
			} catch (Exception e) {
				logger.error("Exception::UserDetailsServiceImpl.Class:loadUserByUsername-1()", e);
			}
		}*/
		
		userDto.setAuthorities(getAuthorities(userDto.getRoles()));
        return UserPrinciple.build(new UserDto(userDto));
    }
    
    public UserPrinciple loadUserDetailByUsername(String username)
            throws UsernameNotFoundException {
    	 UserDto userDto = null;
		try {
			userDto = userDAO.findByUsername(username).orElseThrow(() -> 
			                new UsernameNotFoundException(AppicationConstants.USERNAMENOTFOUNDEXCEPTION)
			);
		} catch (Exception e) {
			logger.error("Exception::UserDetailsServiceImpl.Class:loadUserDetailByUsername()", e);
		}
		
		if(userDto == null || userDto.getAutogenUsersId() == null) {
			throw new UsernameNotFoundException(AppicationConstants.USERNAMENOTFOUNDEXCEPTION);
		} else if("INACTIVE".equalsIgnoreCase(userDto.getStatus())) {
			throw new DisabledException("Your account is inactive. Please contact the system administrator."); 
		}else if("NEW".equalsIgnoreCase(userDto.getStatus())) {
			UserDto userDtoUpdate = new UserDto();
			userDtoUpdate.setAutogenUsersId(userDto.getAutogenUsersId());
			try {
				userDAO.UpdateUserStatus(userDtoUpdate);
			} catch (Exception e) {
				logger.error("Exception::UserDetailsServiceImpl.Class:loadUserDetailByUsername1()", e);
			}
		}
		
		userDto.setAuthorities(getAuthorities(userDto.getRoles()));
        return UserPrinciple.build(new UserDto(userDto));
    }
    
    public Boolean existsByUsername(String username) throws Exception {
    	return userDAO.existsByUsername(username);
    }
    
    public Boolean existsByEmail(String email) throws Exception {
    	return userDAO.existsByEmail(email);
    }
    
    public Collection<GrantedAuthority> getAuthorities(Set<Roles> privileges) {
  	  
        return getGrantedAuthorities(privileges);
    }
 
private List<GrantedAuthority> getGrantedAuthorities(Set<Roles> privileges) {
List<GrantedAuthority> authorities = new ArrayList<>();
for (Roles privilege : privileges) {
    authorities.add(new SimpleGrantedAuthority(privilege.getRolesName()));
}
return authorities;
}

}
