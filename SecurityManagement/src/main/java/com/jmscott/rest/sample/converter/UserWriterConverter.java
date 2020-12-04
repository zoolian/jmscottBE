//package com.jmscott.rest.sample.converter;
//
//import org.springframework.core.convert.converter.Converter;
//import org.springframework.stereotype.Component;
//
//import com.jmscott.rest.SecurityManagement.model.User;
//import com.mongodb.BasicDBObject;
//import com.mongodb.DBObject;
//
//@Component
//public class UserWriterConverter implements Converter<User, DBObject> {
//
//    @Override
//    public DBObject convert(final User user) {
//        final DBObject dbObject = new BasicDBObject();
//        dbObject.put("name", user.getName());
//        dbObject.put("age", user.getAge());
//        if (user.getEmailAddress() != null) {
//            final DBObject emailDbObject = new BasicDBObject();
//            emailDbObject.put("value", user.getEmailAddress().getValue());
//            dbObject.put("email", emailDbObject);
//        }
//        dbObject.removeField("_class");
//        return dbObject;
//    }
//
//}
