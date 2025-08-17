package com.eda.publisher.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eda.publisher.service.PublishService;

@RestController
public class PublisherController {
    @Autowired
    private PublishService publishService;

    @GetMapping("/publish")
    public ResponseEntity<String> publish() {
        Boolean response = publishService.publish();

        return ResponseEntity.ok().body("Process has been " + response.toString());
    }
}