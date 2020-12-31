package com.jmscott.rest.config;

import java.util.Collection;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.jmscott.rest.CascadeSaveMongoEventListener;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

@Configuration
@EnableEncryptableProperties
@EnableMongoRepositories(basePackages = "com.jmscott.rest.repository")
public class MongoConfig extends AbstractMongoClientConfiguration {

	@Override
	protected String getDatabaseName() {
		return "jmscott";
	}

	@Override
	public MongoClient mongoClient() {
		final ConnectionString connectionString = new ConnectionString("mongodb://mongodb-statefulset-0.mongodb-svc:27017/jmscott");
		final String str = "ENC(YC13/Vl68gVRiX/3LzW1woftmR0TKKeJjyWlT7hn5UE5b4DuGO0RQbJxv1gq89Jn)";
		final MongoCredential mongoCredential = MongoCredential.createCredential("admin", "admin", str.toCharArray());
		
		
		final MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
				.applyConnectionString(connectionString)
				.credential(mongoCredential)
				.build();
		return MongoClients.create(mongoClientSettings);
	}
	
	@Override
	public Collection<String> getMappingBasePackages() {
		return Collections.singleton("com.jmscott");
	}
	
	@Bean
	public CascadeSaveMongoEventListener cascadingMongoEventListener() {
		return new CascadeSaveMongoEventListener();
	}
	

	
//	@Override
//    public MongoCustomConversions customConversions() {
//        converters.add(new UserWriterConverter());
//        converters.add(new ZonedDateTimeReadConverter());
//        converters.add(new ZonedDateTimeWriteConverter());
//        return new MongoCustomConversions(converters);
//    }
//
//    @Bean
//    public GridFsTemplate gridFsTemplate() throws Exception {
//        return new GridFsTemplate(mongoDbFactory(), mongoConverter);
//    }
//
//    @Bean
//    MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
//        return new MongoTransactionManager(dbFactory);
//    }
}
