package com.jmscott.rest.model;

import java.util.ArrayList;
import java.util.Collection;

public class UserWithPassword {
	
	private String id;
	
	private String username;
	
	private String password;

	private Person person;
	
	private boolean enabled;

	private Collection<Role> roles = new ArrayList<Role>();

	
	public UserWithPassword() {
		super();
	}

	public UserWithPassword(String id, String username, Person person, boolean enabled, Collection<Role> roles) {
		this.id = id;
		this.username = username;
		this.person = person;
		this.enabled = enabled;
		this.roles = roles;
	}

	public UserWithPassword(String username, Person person, boolean enabled) {
		this.username = username;
		this.person = person;
		this.enabled = enabled;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password +	", person=" + person.toString()
				+ ", enabled=" + enabled + ", roles=" + roles.toString() + "]";
	}
	
		
}
