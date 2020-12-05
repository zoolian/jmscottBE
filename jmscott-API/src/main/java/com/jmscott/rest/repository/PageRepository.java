package com.jmscott.rest.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.jmscott.rest.model.Page;

@Repository
public interface PageRepository extends MongoRepository<Page, String>, QuerydslPredicateExecutor<Page>  {

	Page findByName(String string);
	Optional<Page> findById(String id);
}
