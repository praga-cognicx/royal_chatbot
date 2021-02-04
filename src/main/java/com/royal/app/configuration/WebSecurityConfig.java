package com.royal.app.configuration;

import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.MagicBooleans;
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
import com.royal.app.util.ResourcesLocation;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
    prePostEnabled = true
)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  
  private static final boolean TRACE_MODE = false;
  static String botName = "Bot";
    
    private UserDetailsServiceImpl userDetailsService;
    
    @Bean
    RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }
    
    @Bean
    public Chat chat() {
      ResourcesLocation resource = new ResourcesLocation();
      String resourcesPath = resource.getResourcesPath();
      MagicBooleans.trace_mode = TRACE_MODE;
      Bot bot = new Bot("super", resourcesPath);
      //bot.writeAIMLFiles(); // Read any new aiml files everytime
      Chat chatSession = new Chat(bot);
      bot.brain.nodeStats();
      return chatSession;
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
                .antMatchers("/api/chat/**","/api/ticket/**","/api/login","/api/logout","/api/forgetpassword/request","/api/forgetpassword/reset",/*"/api/login/validateOTP"*/"/v2/api-docs", "/configuration/**","/swagger*/**","/webjars/**").permitAll().anyRequest().anonymous()
                .antMatchers("/api/auth**").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}