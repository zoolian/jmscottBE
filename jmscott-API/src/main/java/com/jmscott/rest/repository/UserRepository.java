package com.jmscott.rest.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.jmscott.rest.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String>, QuerydslPredicateExecutor<User> {

//	@Query("{ 'name' : ?0 }")
//	List<User> findUsersByName(String name);
//
//	@Query("{ 'age' : { $gt: ?0, $lt: ?1 } }")
//	List<User> findUsersByAgeBetween(int ageGT, int ageLT);

//    @Query("{ 'name' : { $regex: ?0 } }")
//    List<User> findUsersByRegexpName(String regexp);
    
	Optional<User> getByUsername(String username);
	User findByUsername(String username);

	Optional<User> getById(String id);
	
//	List<User> findByNameLikeOrderByAgeAsc(String name);
//
//    List<User> findByAgeBetween(int ageGT, int ageLT);
//
//    List<User> findByNameStartingWith(String regexp);
//
//    List<User> findByNameEndingWith(String regexp);
}
