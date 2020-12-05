package com.jmscott.rest.model;

import java.util.ArrayList;
import java.util.Collection;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.jmscott.rest.annotation.CascadeSave;

// CONSIDER: Create a separate object and collection for passwords

//@QueryEntity
@Document
public class User {
	
	@Id
	private String id;
	
	@Indexed(unique = true)
	private String username;
	
	//private String password;
	
	@DBRef
	@CascadeSave
	private Person person;
	
	private boolean enabled;
	
	// don't cascade save roles. on the front end, there's no reason to hold all the fields ...
	// they should be changed in the role manager
	@DBRef
	private Collection<Role> roles = new ArrayList<Role>();

	
	public User() {
		super();
	}

	public User(String id, String username, Person person, boolean enabled, Collection<Role> roles) {
		this.id = id;
		this.username = username;
		this.person = person;
		this.enabled = enabled;
		this.roles = roles;
	}

	public User(String username, Person person, boolean enabled) {
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

//	public String getPassword() {
//		return password;
//	}
//
//	public void setPassword(String password) {
//		this.password = password;
//	}

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
		return "User [id=" + id + ", username=" + username + ", person=" + person.toString()
				+ ", enabled=" + enabled + ", roles=" + roles.toString() + "]";
	}
	
		
}
