package com.jmscott.rest.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Password {
	@Id
	private String id;
	
	private String userId;
	
	private String password;

	public Password(String userId, String password) {
		this.userId = userId;
		this.password = password;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "Password [id=" + id + ", userId=" + userId + ", password=" + password + "]";
	}
	
	
}
