package com.royal.app.services.impl;

import java.math.BigInteger;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.royal.app.model.Roles;
import com.royal.app.shared.dto.UserDto;

public class UserPrinciple implements UserDetails {
	private static final long serialVersionUID = 1L;

	private BigInteger autogenUsersId;
	private String email;
	private String employeeId;
	private String firstName;
	private String lastName;
	private String mobileNumber;
	private String password;
	private String status;
	private Set<Roles> roles = new HashSet<>();
	private Collection<? extends GrantedAuthority> authorities;

	public UserPrinciple(UserDto userDto) {
		this.autogenUsersId = userDto.getAutogenUsersId();
		this.email = userDto.getEmail();
		this.employeeId = userDto.getEmployeeId();
		this.firstName = userDto.getFirstName();
		this.lastName = userDto.getLastName();
		this.mobileNumber = userDto.getMobileNumber();
		this.password = userDto.getPassword();
		this.status = userDto.getStatus();
		this.roles = userDto.getRoles();
		this.authorities = userDto.getAuthorities();
	}

	public static UserPrinciple build(UserDto userDto) {
		return new UserPrinciple(userDto);
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((autogenUsersId == null) ? 0 : autogenUsersId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserPrinciple other = (UserPrinciple) obj;
		if (autogenUsersId == null) {
			if (other.autogenUsersId != null)
				return false;
		} else if (!autogenUsersId.equals(other.autogenUsersId))
			return false;
		return true;
	}

	public BigInteger getAutogenUsersId() {
		return autogenUsersId;
	}

	public void setAutogenUsersId(BigInteger autogenUsersId) {
		this.autogenUsersId = autogenUsersId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Set<Roles> getRoles() {
		return roles;
	}

	public void setRoles(Set<Roles> roles) {
		this.roles = roles;
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}

}