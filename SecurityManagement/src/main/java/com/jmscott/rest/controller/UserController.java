package com.jmscott.rest.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.jmscott.rest.model.Password;
import com.jmscott.rest.model.QUser;
import com.jmscott.rest.exception.ResourceNotFoundException;
import com.jmscott.rest.model.User;
import com.jmscott.rest.repository.PasswordRepository;
import com.jmscott.rest.repository.UserRepository;
import com.querydsl.core.types.dsl.BooleanExpression;

//TODO: double check that responses are to industry standard

@RestController
@RequestMapping(path = "/security/users")
@CrossOrigin(origins= {"http://localhost:3001", "http://localhost:3000"}, allowCredentials = "true")
public class UserController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordRepository passwordRepository;
	
//	@Autowired
//	private MongoTemplate mongoTemplate;
	
	//private QUser globalUser = new QUser("globalUser");
	//private BooleanExpression filterByEnabled = globalUser.enabled.isTrue();
	
	@GetMapping
	@CrossOrigin
	public List<User> getUsersAll(@RequestParam(required = false) boolean showDisabled) {
//		QUser qUser = new QUser("user");
//		BooleanExpression filterByUsername = qUser.username.eq(username);
//		BooleanExpression filterByEnabled = showDisabled ? null : globalUser.enabled.isTrue();
//		
//		List<User> users = (List<User>) this.userRepository.findAll(filterByUsername.and(filterByEnabled));
		
		List<User> u = userRepository.findAll();
		return u;
	}
	
	@GetMapping(path = "/username/{username}")	// TODO: add url param for %like
	public ResponseEntity<User> getUserByUsername(@PathVariable String username) throws ResourceNotFoundException {
		User user = userRepository.getByUsername(username).orElseThrow(
				() -> new ResourceNotFoundException("User not found: " + username)
			);
			
			return ResponseEntity.ok(user);
	}
	
	@GetMapping(path = "/lastName/{lastName}")	// FIX ME: like
	public List<User> getUsersByLastName(@PathVariable String lastName) {
		QUser qUser = new QUser("user");
		BooleanExpression filterByLastName = qUser.person.lastName.eq(lastName);
		
		List<User> users = (List<User>) this.userRepository.findAll(filterByLastName);
		
		return users;
	}
	
	@GetMapping(path = "/age-range/{ageLow}/{ageHigh}")
	public List<User> getUsersByAgeRange(@PathVariable int ageLow, @PathVariable int ageHigh) {
		QUser qUser = new QUser("user");
		BooleanExpression filterByAge = qUser.person.age.between(ageLow, ageHigh);
		
		List<User> users = (List<User>) this.userRepository.findAll(filterByAge);
		
		return users;
	}
	
	// qUser.roles.any().role.eq("ADMIN")	// use any() for array in object
	
	@GetMapping(path = "/id/{id}")
	public ResponseEntity<User> getUserById(@PathVariable String id) throws ResourceNotFoundException {
		User user = userRepository.findById(id).orElseThrow(
			() -> new ResourceNotFoundException("User not found with id " + id)
		);
		
		return ResponseEntity.ok(user);	// .ok().build(user)
	}
	
	@PostMapping
	public ResponseEntity<Void> createUser(@Validated @RequestBody User user) {
		user.setId(null); // guarantee that mongo is creating id
		user.setEnabled(true);	// guarantee a new user is enabled. CONSIDER: one might want a new user to be disabled?
		
		User newUser = userRepository.save(user);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(newUser.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@PutMapping(path = "/{id}")
	public ResponseEntity<User> updateUser(
			@PathVariable String id,
			@Validated @RequestBody User userDetails) throws ResourceNotFoundException {
		userRepository.findById(id).orElseThrow( () -> new ResourceNotFoundException("User not found with id " + id) );
		User savedUser = userRepository.save(userDetails);
		
		return new ResponseEntity<User>(savedUser, HttpStatus.OK);
	}
	
	@PutMapping(path = "/secret/{id}")
	public ResponseEntity<User> updatePassword(
			@PathVariable String id,
			@Validated @RequestBody Password passwordDetails) throws ResourceNotFoundException {
		User savedUser = userRepository.findById(id).orElseThrow( () -> new ResourceNotFoundException("User not found with id " + id) );
		passwordRepository.save(passwordDetails);
		
		return new ResponseEntity<User>(savedUser, HttpStatus.OK);
	}
	
//	@PutMapping(path = "/{id}")
//	public ResponseEntity<String> updateUser(
//			@PathVariable String id,
//			@Validated @RequestBody User userDetails) throws ResourceNotFoundException {
//		Query query = new Query();
//		Update update = new Update();
//		
//		query.addCriteria(Criteria.where("id").is(id));
//		// all this noise is here because we're not juggling the password on the frontend, and it's coming in as null because of it
//		update.set("id", id);
//		update.set("username", userDetails.getUsername());
//		update.set("person", userDetails.getPerson());
//		update.set("enabled", userDetails.isEnabled());
//		update.set("roles", userDetails.getRoles());
//		if(userDetails.getPassword() != null) {
//			update.set("password", userDetails.getPassword());
//		}
//		User newUser = mongoTemplate.findAndModify(query, update, User.class);
//
//		return new ResponseEntity<String>(newUser.getUsername(), HttpStatus.OK);
//	}
	
	@DeleteMapping(path = "/{id}")
	public ResponseEntity<User> deleteUser(@PathVariable String id) throws ResourceNotFoundException {
		User deletedUser = userRepository.findById(id).orElseThrow( () -> new ResourceNotFoundException("User not found with id " + id) );
		userRepository.deleteById(id);
		
		return new ResponseEntity<User>(deletedUser, HttpStatus.OK);
	}
}
