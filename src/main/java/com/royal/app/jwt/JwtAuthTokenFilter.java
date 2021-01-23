package com.royal.app.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import com.royal.app.services.UserService;
import com.royal.app.shared.dto.TokenDetailsDto;
 
public class JwtAuthTokenFilter extends OncePerRequestFilter {
 
    @Autowired
    private JwtProvider tokenProvider;
 
    @Autowired
    private UserService userService;
    
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthTokenFilter.class);
 
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                    HttpServletResponse response, 
                    FilterChain filterChain) 
			throws ServletException, IOException {
		try {

			String jwt = getJwt(request);
			if (jwt != null) {
					Object[] tokenObj = tokenProvider.validateJwtTokenObj(jwt, response);
					boolean tokenFlag = (boolean) tokenObj[0];
					if (tokenFlag) {
						String username = tokenProvider.getUserNameFromJwtToken(jwt);
						TokenDetailsDto tokenDetailsDto = new TokenDetailsDto();
						tokenDetailsDto.setEmployeeId(username);
						tokenDetailsDto.setToken(jwt);
						boolean tokenExist = userService.checkExistingTokenDetails(tokenDetailsDto);
						if (tokenExist && username != null && !username.isEmpty()) {
							UserDetails userDetails = tokenProvider.getUserDetailsFromJwtToken(jwt);
							UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
									userDetails, null, userDetails.getAuthorities());
							authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
							SecurityContextHolder.getContext().setAuthentication(authentication);
						}
					} else {
						response = (HttpServletResponse) tokenObj[1];
					}
			} 
			
		} catch (Exception e) {
			logger.error("Can NOT set user authentication -> Message: {}", e);
		}

		filterChain.doFilter(request, response);
	}
 
    private String getJwt(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if(authHeader == null || authHeader.isEmpty())
          authHeader = request.getHeader("authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
          return authHeader.replace("Bearer ","");
        }
 
        return null;
    }
}
