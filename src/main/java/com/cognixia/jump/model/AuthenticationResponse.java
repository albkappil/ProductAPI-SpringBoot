package com.cognixia.jump.model;

// response object for when /authenticate POST request is called and returns the jwt created
public class AuthenticationResponse {
	
	private String email;
    private String accessToken;
 
    public AuthenticationResponse() { }
     
    public AuthenticationResponse(String email, String accessToken) {
        this.email = email;
        this.accessToken = accessToken;
    }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
    
    
    

}
