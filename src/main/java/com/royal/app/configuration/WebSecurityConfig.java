package com.royal.app.configuration;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import com.royal.app.jwt.JwtAuthEntryPoint;
import com.royal.app.jwt.JwtAuthTokenFilter;
import com.royal.app.services.impl.UserDetailsServiceImpl;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
    prePostEnabled = true
)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    
    private UserDetailsServiceImpl userDetailsService;
    
    @Bean
    RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }
    
    @Autowired
    public void setUserDetailsService(UserDetailsServiceImpl userDetailsService) {
            this.userDetailsService = userDetailsService;
    }
 
    @Autowired
    private JwtAuthEntryPoint unauthorizedHandler;
 
    @Bean
    public JwtAuthTokenFilter authenticationJwtTokenFilter() {
        return new JwtAuthTokenFilter();
    }
 
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
    @Bean
    public AuthenticationProvider authenticationProvider() throws Exception {
      DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
      provider.setHideUserNotFoundExceptions(false);
      provider.setUserDetailsService(userDetailsService);
      provider.setPasswordEncoder(passwordEncoder());
      return provider;
    }
 
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().
                authorizeRequests()
                .antMatchers("/chatbot/**",/*"/ratesheet/update/mappedhoteldropdown",,"/ratesheet/updatetype/dropdown", "/ratesheet/update/updateRatesheetUpdates","/ratesheet/update/saveRatesheetUpdates","/ratesheet/mgt/priority/list","/ratesheet/mgt/priority/update","/ratesheet/mgt/view","/ratesheet/Maps/create","/ratesheet/maps/list","/ratesheet/maps/dropdowns","/ratesheet/agentdropdown", "/ratesheet/maps/list","/ratesheet/mgt/update","/ratesheet/mgt/list", "/file/ratesheet/uploadFile","/ratesheet/getRateSheetDet", "/ratesheet/mgt/create","/api/token/authenticate",*/"/ratesheet/getRateSheetDetNew","/ratesheet/getRateSheetDet","/file/ratesheet/uploadFile","/api/login","/api/logout","/api/forgetpassword/request","/api/forgetpassword/reset",/*"/api/login/validateOTP"*/"/v2/api-docs", "/configuration/**","/swagger*/**","/webjars/**").permitAll().anyRequest().anonymous()
                .antMatchers("/api/auth**").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}