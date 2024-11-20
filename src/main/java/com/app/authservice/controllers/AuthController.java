package com.app.authservice.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.WebRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class AuthController {

    @GetMapping("/login")
    String getLogin() {
        return "login";
    }

    @GetMapping("/register")
    public String getRegister(WebRequest request, Model model) {
        return "register";
    }
}