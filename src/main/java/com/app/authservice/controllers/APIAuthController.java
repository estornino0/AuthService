package com.app.authservice.controllers;

import com.app.authservice.dto.UserDto;
import com.app.authservice.security.JWTUtil;
import com.app.authservice.service.UserDetailServiceImpl;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("/api/auth")
public class APIAuthController {

    @Autowired
    private UserDetailServiceImpl userService;

    @Autowired private AuthenticationManager authManager;

    @Autowired private JWTUtil JWTUtil;

    @PostMapping("/login")
    public ResponseEntity<Object> loginHandler(@Valid @RequestBody UserDto userDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return bindingErrorResponse(bindingResult);
        }

        Map<String, Object> responseBody = new HashMap<>();
        try {
            UsernamePasswordAuthenticationToken authInputToken =
                    new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword());

            authManager.authenticate(authInputToken);

            // Authentication was successful
            String token = JWTUtil.generateToken(userDto.getEmail());
            responseBody.put("access_token", token);
            responseBody.put("message", "Login successful");
            return ResponseEntity.ok(responseBody);

        } catch (AuthenticationException authExc){
            // Auhentication Failed
            responseBody.put("message", "Wrong email or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerHandler(@Valid @RequestBody UserDto userDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return bindingErrorResponse(bindingResult);
        }
        try {
            userService.registerNewUser(userDto.getEmail(), userDto.getPassword());
        } catch (IllegalStateException e) {
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(responseBody);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    private static @NotNull ResponseEntity<Object> bindingErrorResponse(BindingResult bindingResult) {
        List<String> errors = new ArrayList<>();
        bindingResult.getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()));
        Map<String, List<String>> responseBody = new HashMap<>();
        responseBody.put("message", errors);
        return ResponseEntity.badRequest().body(responseBody);
    }
}