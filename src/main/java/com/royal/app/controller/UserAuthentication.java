/*package com.royal.app.controller;

import java.sql.Timestamp;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.royal.app.constants.StatusCodeConstants;
import com.royal.app.jwt.JwtProvider;
import com.royal.app.mail.EmailService;
import com.royal.app.message.request.LoginForm;
import com.royal.app.message.request.LogoutRequest;
import com.royal.app.message.request.ValidateOtpRequest;
import com.royal.app.message.response.GenericResponse;
import com.royal.app.message.response.JwtResponse;
import com.royal.app.services.OtpService;
import com.royal.app.services.RolesService;
import com.royal.app.services.UserService;
import com.royal.app.services.impl.UserDetailsServiceImpl;
import com.royal.app.services.impl.UserPrinciple;
import com.royal.app.shared.dto.TokenDetailsDto;
import com.royal.app.util.UserInfo;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class UserAuthentication {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	RolesService rolesService;

	@Autowired
	UserService userService;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	OtpService otpService;

	@Autowired
	UserInfo userInfo;
	
	@Autowired
    JwtProvider jwtProvider;
    
    @Value("${app.jwtExpiration}")
    private int jwtExpiration;
    
    @Autowired
    EmailService emailService;
    
    
	private static final Logger logger = LoggerFactory.getLogger(UserAuthentication.class);

	@PostMapping("/login")
    public ResponseEntity<GenericResponse> authenticateUser(@Valid @RequestBody LoginForm loginRequest, HttpServletRequest request) throws Exception {
    	request.setAttribute("tempUserName", loginRequest.getUsername());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        
        UserPrinciple userPrinciple =   (UserPrinciple) authentication.getPrincipal();
        GenericResponse genericResponse = new GenericResponse();
  		SecurityContextHolder.getContext().setAuthentication(authentication);
  	  //Update login Attempt
        Object[] loginInfo = new Object[10];
        loginInfo[0] = userPrinciple.getEmployeeId();
        loginInfo[1] = true;
        loginInfo[3] = null;
        loginInfo[5] = "System";
        loginInfo[6] = "System";
        try {
            userService.saveOrUpdateLoginDetails(true, loginInfo);
        } catch (Exception e) {
            logger.error("Exception::UserAuthentication.Class:authenticateUser()", e);
        }
        
		String jwtToken = jwtProvider.generateJwtToken(authentication);
        Date date = new Date((new Date()).getTime() + jwtExpiration);
        long dateTime = date.getTime();
        Timestamp expiryDate = new Timestamp(dateTime);
        TokenDetailsDto tokenDetailsDto = new TokenDetailsDto();
        tokenDetailsDto.setEmployeeId(userPrinciple.getEmployeeId());
        tokenDetailsDto.setExpiryDate(expiryDate);
        tokenDetailsDto.setExpirySeconds(jwtExpiration);
        tokenDetailsDto.setToken(jwtToken);
        tokenDetailsDto.setRefreshToken("");
        tokenDetailsDto.setStatus("Active");
        tokenDetailsDto.setCreatedBy("System");
        userService.saveTokenDetails(tokenDetailsDto);

        int otpNumber = otpService.generateOTP(userPrinciple.getEmployeeId());
        String mailBody = "Dear "+userPrinciple.getFirstName()+" "+userPrinciple.getLastName()+","
                +"\n"+"Your login otp is "+otpNumber;
                emailService.sendSimpleMessage(userPrinciple.getEmail(), "Royal User Login OTP::"+userPrinciple.getEmployeeId(), mailBody);
        System.out.println("OTP::" + userPrinciple.getEmployeeId() + "::" + otpNumber);
        genericResponse.setStatus(StatusCodeConstants.SUCCESS);
        genericResponse.setMessage(
                "OTP sent to registered Email Id. Please enter valid OTP to verify.");
        genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
        
        genericResponse.setStatus(StatusCodeConstants.SUCCESS);
        genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
        genericResponse.setMessage("Logged in Successfully");
        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setAccessToken(jwtToken);
        jwtResponse.setUserName(userPrinciple.getEmployeeId());
        jwtResponse.setRoles(userPrinciple.getAuthorities().toString());
        jwtResponse.setExpiryDate(expiryDate);
        jwtResponse.setExpirySeconds(String.valueOf(jwtExpiration));
        jwtResponse.setFirstName(userPrinciple.getFirstName());
        jwtResponse.setLastName(userPrinciple.getLastName());
        jwtResponse.setMobileNumber(userPrinciple.getMobileNumber());
        genericResponse.setValue(new JwtResponse(jwtResponse));
        return ResponseEntity.ok(new GenericResponse(genericResponse));
    }

	@PostMapping("/logout")
	public ResponseEntity<GenericResponse> logout(@Valid @RequestBody LogoutRequest logoutRequest,
			HttpServletRequest request) throws Exception {

		boolean logoutStatus = false;
		// Update login Attempt
		Object[] loginInfo = new Object[10];
		loginInfo[0] = logoutRequest.getEmployeeId();
		loginInfo[1] = false;
		loginInfo[3] = true;
		loginInfo[5] = "System";
		loginInfo[6] = logoutRequest.getEmployeeId();
		try {
			Object[] object = userService.saveOrUpdateLoginDetails(false, loginInfo);
			if (object != null && object[0] != null) {
				logoutStatus = (boolean) object[0];
			}

		} catch (Exception e) {
			logger.error("Exception::UserAuthentication.Class:logout()", e);
		}

		if (logoutStatus) {
			TokenDetailsDto tokenDetailsDto = new TokenDetailsDto();
			tokenDetailsDto.setToken(logoutRequest.getToken());
			tokenDetailsDto.setEmployeeId(logoutRequest.getEmployeeId());
			tokenDetailsDto = userService.updateTokenStatus(tokenDetailsDto);
				logoutStatus = tokenDetailsDto.isFlag();
		}
		GenericResponse genericResponse = new GenericResponse();
		if(logoutStatus) {
		genericResponse.setStatus(StatusCodeConstants.SUCCESS);
		genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
		genericResponse.setMessage(logoutRequest.getEmployeeId() + " Logged Out Successfully!!!");
		genericResponse.setValue("");
		}
		return ResponseEntity.ok(new GenericResponse(genericResponse));
	}
	
	@PostMapping(path="/login/validateOTP")
    public ResponseEntity<GenericResponse> validateOTP(@Valid @RequestBody ValidateOtpRequest validateOTP) throws Exception {
      GenericResponse genericResponse = new GenericResponse();
      final String SUCCESS = "Entered Otp is valid";
      final String FAIL = "Entered Otp is NOT valid. Please Retry!";

      try {
          logger.info(" Otp Number : " + validateOTP.getOtpNumber());
          // Validate the Otp
          if (validateOTP.getOtpNumber() >= 0) {
              int serverOtp = otpService.getOtp(validateOTP.getEmployeeId());
              if (serverOtp > 0) {
                  if (validateOTP.getOtpNumber() == serverOtp) {
                      otpService.clearOTP(validateOTP.getEmployeeId());
                      genericResponse.setStatus(StatusCodeConstants.SUCCESS);
                      genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
                      genericResponse.setMessage(SUCCESS);
                      genericResponse.setValue(null);
                  } 
              } else {
                  genericResponse.setStatus(StatusCodeConstants.FAILURE);
                  genericResponse.setError(StatusCodeConstants.FAILURE_STR);
                  genericResponse.setMessage(FAIL);
                  genericResponse.setValue(null);
              }
          }
      } catch (Exception e) {
          genericResponse.setStatus(StatusCodeConstants.FAILURE);
          genericResponse.setError(StatusCodeConstants.FAILURE_STR);
          genericResponse.setMessage("OTP validation failure. Please contact admin.");
          genericResponse.setValue(null);
          logger.error("Exception::UserAuthentication.Class:validateOTP()", e);
      }
      return ResponseEntity.ok(new GenericResponse(genericResponse));

  }


   @PostMapping("/token/authenticate")
	public ResponseEntity<GenericResponse> tokenAuthenticate(
			@Valid @RequestBody AuthenticateRequest authenticateRequest, HttpServletResponse response)
			throws Exception {
		GenericResponse genericResponse = new GenericResponse();
		String token = getToken(authenticateRequest.getToken());
		if (token != null && !token.isEmpty()) {
			Object[] tokenObj = jwtProvider.validateJwtTokenObj(token, response);
			boolean tokenFlag = (boolean) tokenObj[0];
			if (tokenFlag) {
				String username = jwtProvider.getUserNameFromJwtToken(token);
				TokenDetailsDto tokenDetailsDto = new TokenDetailsDto();
				tokenDetailsDto.setEmployeeId(username);
				tokenDetailsDto.setToken(token);
				boolean tokenExist = userService.checkExistingTokenDetails(tokenDetailsDto);
				if (tokenExist && username != null && !username.isEmpty()) {
					UserDetails userDetails = jwtProvider.getUserDetailsFromJwtToken(token);
					UserPrinciple userPrinciple = (UserPrinciple) userDetails;
					if (userPrinciple != null) {
						AuthenticateResponse authenticateResponse = new AuthenticateResponse(
								userPrinciple.getAutogenUsersId(), userPrinciple.getEmail(),
								userPrinciple.getEmployeeId(), userPrinciple.getFirstName(),
								userPrinciple.getLastName(), userPrinciple.getMobileNumber(),
								userPrinciple.getStatus());
						genericResponse.setStatus(StatusCodeConstants.SUCCESS);
						genericResponse.setMessage("Token verification success!!!");
						genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
						genericResponse.setValue(authenticateResponse);
					}
				} else {
					genericResponse.setStatus(StatusCodeConstants.FAILURE);
					genericResponse.setMessage("Invalid token. Verification failure!!!");
					genericResponse.setError(StatusCodeConstants.FAILURE_STR);

				}
			}

		} else {
			genericResponse.setStatus(StatusCodeConstants.FAILURE);
			genericResponse.setMessage("Invalid token. Verification failure!!!");
			genericResponse.setError(StatusCodeConstants.FAILURE_STR);
		}
		return ResponseEntity.ok(new GenericResponse(genericResponse));

	}
   
	private String getToken(String token) {
		if (token != null && token.startsWith("Bearer ")) {
			return token.replace("Bearer ", "");
		}
		return null;
	}



	@PostMapping(path="/forgetpassword/request")
	public ResponseEntity<GenericResponse> forgetPasswordRequest(
			@Valid @RequestBody ForgetPasswordRequest forgetPasswordRequest, HttpServletResponse response)
			throws Exception {
		GenericResponse genericResponse = new GenericResponse();
		try {
			UserPrinciple userPrincipal = userDetailsService
					.loadUserDetailByUsername(forgetPasswordRequest.getEmployeeId());
			if (userPrincipal != null) {
				if (userPrincipal.getEmail() == null || userPrincipal.getEmail().isEmpty()) {
					genericResponse.setStatus(StatusCodeConstants.FAILURE);
					genericResponse.setError(StatusCodeConstants.FAILURE_STR);
					genericResponse.setMessage("Please contact the admin for the password.");
					genericResponse.setValue(null);
				} else {
					int otpNumber = otpService.generateOTP(forgetPasswordRequest.getEmployeeId());
					String mailBody = "Hi "+forgetPasswordRequest.getEmployeeId()+","
							+""+"Password reset otp is "+otpNumber;
							emailService.sendSimpleMessage(userPrincipal.getEmail(), "User Password Reset OTP::"+forgetPasswordRequest.getEmployeeId(), mailBody);
					System.out.println("OTP::" + forgetPasswordRequest.getEmployeeId() + "::" + otpNumber);
					genericResponse.setStatus(StatusCodeConstants.SUCCESS);
					genericResponse.setMessage(
							"OTP sent to registered Email Id. Please enter OTP and Validate to reset password.");
					genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
					genericResponse.setValue(null);
				}
			}
		} catch (Exception e) {
			genericResponse.setStatus(StatusCodeConstants.FAILURE);
			genericResponse.setError(StatusCodeConstants.FAILURE_STR);
			genericResponse.setMessage("Forget password failure. Please contact admin.");
			genericResponse.setValue(null);
			logger.error("Exception::UserAuthentication.Class:forgetPasswordRequest()", e);
		}
		return ResponseEntity.ok(new GenericResponse(genericResponse));

	}

	@PostMapping(path="/forgetpassword/validateOTP")
	public ResponseEntity<GenericResponse> validateOTP(@Valid @RequestBody ValidateOtpRequest validateOTP,
			HttpServletResponse response) throws Exception {
		GenericResponse genericResponse = new GenericResponse();
		final String SUCCESS = "Entered Otp is valid";
		final String FAIL = "Entered Otp is NOT valid. Please Retry!";

		try {
			logger.info(" Otp Number : " + validateOTP.getOtpNumber());
			// Validate the Otp
			if (validateOTP.getOtpNumber() >= 0) {
				int serverOtp = otpService.getOtp(validateOTP.getEmployeeId());
				if (serverOtp > 0) {
					if (validateOTP.getOtpNumber() == serverOtp) {
						otpService.clearOTP(validateOTP.getEmployeeId());
						genericResponse.setStatus(StatusCodeConstants.SUCCESS);
						genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
						genericResponse.setMessage(SUCCESS);
						genericResponse.setValue(null);
					} else {
						genericResponse.setStatus(StatusCodeConstants.SUCCESS);
						genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
						genericResponse.setMessage(SUCCESS);
						genericResponse.setValue(null);
					}
				} else {
					genericResponse.setStatus(StatusCodeConstants.FAILURE);
					genericResponse.setError(StatusCodeConstants.FAILURE_STR);
					genericResponse.setMessage(FAIL);
					genericResponse.setValue(null);
				}
			}
		} catch (Exception e) {
			genericResponse.setStatus(StatusCodeConstants.FAILURE);
			genericResponse.setError(StatusCodeConstants.FAILURE_STR);
			genericResponse.setMessage("OTP validation failure. Please contact admin.");
			genericResponse.setValue(null);
			logger.error("Exception::UserAuthentication.Class:validateOTP()", e);
		}
		return ResponseEntity.ok(new GenericResponse(genericResponse));

	}

	@PostMapping(path="/forgetpassword/reset")
	public ResponseEntity<GenericResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest,
			HttpServletResponse response) throws Exception {
		GenericResponse genericResponse = new GenericResponse();
		try {
			if (resetPasswordRequest.getPassword().equals(resetPasswordRequest.getConfirmPassword())) {
				UserDto userDto = new UserDto();
				userDto.setEmployeeId(resetPasswordRequest.getEmployeeId());
				userDto.setPassword(encoder.encode(resetPasswordRequest.getPassword()));
				boolean resetStatus = userService.resetPassword(userDto);
				if (resetStatus) {
					genericResponse.setStatus(StatusCodeConstants.SUCCESS);
					genericResponse.setError(StatusCodeConstants.SUCCESS_STR);
					genericResponse.setMessage("Password reset successfully.");
					genericResponse.setValue(null);
				} else {
					genericResponse.setStatus(StatusCodeConstants.FAILURE);
					genericResponse.setError(StatusCodeConstants.FAILURE_STR);
					genericResponse.setMessage("Reset password failure. Please contact admin.");
					genericResponse.setValue(null);
					logger.error("Exception::Reset password failure. Please contact admin.");
				}
			} else {
				genericResponse.setStatus(StatusCodeConstants.FAILURE);
				genericResponse.setError(StatusCodeConstants.FAILURE_STR);
				genericResponse.setMessage("The Confirm password confirmation does not match.");
				genericResponse.setValue(null);
			}
		} catch (Exception e) {
			genericResponse.setStatus(StatusCodeConstants.FAILURE);
			genericResponse.setError(StatusCodeConstants.FAILURE_STR);
			genericResponse.setMessage("Reset password failure. Please contact admin.");
			genericResponse.setValue(null);
			logger.error("Exception::UserAuthentication.Class:resetPassword()", e);
		}
		return ResponseEntity.ok(new GenericResponse(genericResponse));
	}
}
*/