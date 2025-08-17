package com.eda.publisher.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PublishServiceImpl {
// public class PublishServiceImpl implements PublishService {
     @Autowired
     private PubSubTemplate pubSubTemplate;

//     @Override
//     public boolean publish() {
//         String uuid = UUID.randomUUID().toString();

//         //System.out.println("Publishing message: " + messagePayload + " to topic: " + topicName);
//         //this.pubSubTemplate.publish("documents.create", uuid);

        // return true;
    // }
}