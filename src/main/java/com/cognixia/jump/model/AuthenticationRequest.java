package com.cognixia.jump.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

// model object used to send in username & password when user first authenticates and needs to create JWT on
// the /authenticate POST request 
public class AuthenticationRequest {
	
	@NotNull @Email @Length(min = 5, max = 50)
	private String username;
	
	@NotNull @Length(min = 5)
	private String password;
	
	public AuthenticationRequest() {
		
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
