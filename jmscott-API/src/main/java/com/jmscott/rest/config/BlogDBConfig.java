//package com.jmscott.rest.config;
//
//import java.util.Collection;
//import java.util.Collections;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
//import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
//
//import com.jmscott.rest.CascadeSaveMongoEventListener;
//import com.mongodb.ConnectionString;
//import com.mongodb.MongoClientSettings;
//import com.mongodb.client.MongoClient;
//import com.mongodb.client.MongoClients;
//
//@Configuration
//@EnableMongoRepositories(basePackages = "com.jmscott.rest.repository.blog") //mongoTemplateRef = "blogDBTemplate", 
//public class BlogDBConfig extends AbstractMongoClientConfiguration {
//
//	@Override
//	protected String getDatabaseName() {
//		return "blog";
//	}
//
//	@Override
//	public MongoClient mongoClient() {
//		final ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017/blog");
//		final MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
//				.applyConnectionString(connectionString).build();
//		return MongoClients.create(mongoClientSettings);
//	}
//	
//	@Override
//	public Collection<String> getMappingBasePackages() {
//		return Collections.singleton("com.jmscott.rest.repository.blog");
//	}	
//
//	
////	@Override
////    public MongoCustomConversions customConversions() {
////        converters.add(new UserWriterConverter());
////        converters.add(new ZonedDateTimeReadConverter());
////        converters.add(new ZonedDateTimeWriteConverter());
////        return new MongoCustomConversions(converters);
////    }
////
////    @Bean
////    public GridFsTemplate gridFsTemplate() throws Exception {
////        return new GridFsTemplate(mongoDbFactory(), mongoConverter);
////    }
////
////    @Bean
////    MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
////        return new MongoTransactionManager(dbFactory);
////    }
//}
