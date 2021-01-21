package com.jmscott.rest.controller;

import java.net.URI;
import java.util.Date;
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

import com.jmscott.rest.exception.ResourceNotFoundException;
import com.jmscott.rest.model.Post;
import com.jmscott.rest.repository.PostRepository;
import com.jmscott.rest.repository.UserRepository;

@RestController
@RequestMapping(path = "/blog")
@CrossOrigin(origins = {"http://localhost:3001", "http://localhost:3000", "https://security.jmscottnovels.com", "https://blog.jmscottnovels.com"}, allowCredentials = "true")
public class BlogController {
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping(path = "/posts/user/{id}")
	public List<Post> getPostsByUser (@PathVariable String id) throws ResourceNotFoundException {
		userRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("No user with id: " + id));
		return postRepository.findByUserId(id);
	}
	
	@GetMapping(path = "/posts")
	//@CrossOrigin
	public List<Post> getAllPosts (@RequestParam(required = false) Integer recentNum) {
//		QPost post = new QPost("post");
//		BooleanExpression filterByRecent =  post.
		return postRepository.findAll();
	}
	
	@GetMapping(path = "/posts/{id}")
	public ResponseEntity<Post> getPost (@PathVariable String id) throws ResourceNotFoundException {
		
		Post post = postRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("No post with id: " + id));
		
		return ResponseEntity.ok().body(post);
	}
	
	@PostMapping(path = "/posts")
	public ResponseEntity<Void> createBlogPost (@Validated @RequestBody Post post) {
		post.setId(null);
		post.setCreateDate(new Date());		// new post. set create date here ... we don't want to default this in the constructor
		post.setModifiedDate(new Date());	// set immediately before db write
		post.setHidden(false);
		Post newPost = postRepository.save(post);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
			.buildAndExpand(newPost.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@PutMapping(path = "/posts/{id}")
	public ResponseEntity<Post> updateBlogPost (
			@PathVariable String id,
			@Validated @RequestBody Post post) throws ResourceNotFoundException {
		
		postRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("No post with id: " + id));
		
		post.setModifiedDate(new Date());	// set immediately before db write
		final Post newPost = postRepository.save(post);
		
		return new ResponseEntity<Post>(newPost, HttpStatus.OK);
		
	}
	
	@DeleteMapping(path = "/posts/{id}")
	public ResponseEntity<Post> deleteBlogPost (@PathVariable String id) throws ResourceNotFoundException {
		
		Post deletedPost = postRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("No post with id: " + id));
		
		postRepository.deleteById(id);
		return new ResponseEntity<Post>(deletedPost, HttpStatus.OK);
		
	}

}
