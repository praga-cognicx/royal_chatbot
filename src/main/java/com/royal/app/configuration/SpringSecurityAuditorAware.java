package com.royal.app.configuration;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.royal.app.services.impl.UserPrinciple;

@Service
public class SpringSecurityAuditorAware {

	public UserPrinciple getCurrentAuditor() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()
				|| authentication.getPrincipal().equals("anonymousUser")) {
			return null;
		}

		return ((UserPrinciple) authentication.getPrincipal());
	}

	public String getUserName() {
		if (getCurrentAuditor().getEmployeeId() != null) {
			return getCurrentAuditor().getEmployeeId();
		}
		return "";
	}

}
