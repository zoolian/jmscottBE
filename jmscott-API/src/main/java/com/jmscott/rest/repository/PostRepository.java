package com.jmscott.rest.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.jmscott.rest.model.Post;

//https://docs.spring.io/spring-data/mongodb/docs/1.2.0.RELEASE/reference/html/mongo.repositories.html
@Repository
public interface PostRepository extends MongoRepository<Post, Long>, QuerydslPredicateExecutor<Post>{

	List<Post> findByUserId(String id);

	Optional<Post> findById(String id);

	void deleteById(String id);
	
}
