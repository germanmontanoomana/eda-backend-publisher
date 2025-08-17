package com.eda.publisher.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublisherController {
    @GetMapping("/publish")
    public ResponseEntity<String> publish() {
        String response = "something else into my response and update";

        return ResponseEntity.ok().body(response);
    }
}