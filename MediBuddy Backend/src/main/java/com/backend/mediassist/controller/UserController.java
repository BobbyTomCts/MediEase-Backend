package com.backend.mediassist.controller;

import com.backend.mediassist.model.LoginResponse;
import com.backend.mediassist.model.User;
import com.backend.mediassist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.registerUser(user);
    }
    
    // Login user with JWT token
    @PostMapping("/login")
    public LoginResponse login(@RequestParam String email, @RequestParam String password) {
        return userService.loginUser(email, password);
    }
    
    // Validate token and get user details
    @GetMapping("/validate")
    public User validateToken(@RequestHeader("Authorization") String token) {
        // Remove "Bearer " prefix if present
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return userService.validateTokenAndGetUser(token);
    }
    
    // Get user by ID
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }
    
    // Check if user is admin
    @GetMapping("/isAdmin/{userId}")
    public boolean isAdmin(@PathVariable Long userId) {
        return userService.isAdmin(userId);
    }
}