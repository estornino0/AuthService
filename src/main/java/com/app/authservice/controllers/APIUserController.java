package com.app.authservice.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController("/api")
public class APIUserController {

    @GetMapping("/protected")
    public ResponseEntity<Object> getProtected() {
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Protected endpoint accessed");
        return ResponseEntity.ok(responseBody);
    }

}
