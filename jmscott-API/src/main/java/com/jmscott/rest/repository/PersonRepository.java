package com.jmscott.rest.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.jmscott.rest.model.Person;

@Repository
public interface PersonRepository extends MongoRepository<Person, String>, QuerydslPredicateExecutor<Person> {
	Person findByEmail(String email);
}
