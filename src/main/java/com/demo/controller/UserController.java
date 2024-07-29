package com.demo.controller;

import com.demo.dto.LoginResponse;
import com.demo.dto.UpdateUser;
import com.demo.dto.UserLogin;
import com.demo.model.User;
import com.demo.service.UserService;
import com.demo.utility.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/user")
@Tag(name = "User Controller", description = "Controller for user operations")
public class UserController {

    private final UserService userService;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserController(UserService userService, JWTUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @Operation(summary = "Save user", description = "Endpoint to save a new user")
    @PostMapping("/save")
    public ResponseEntity<User> saveUser(@Valid @RequestBody User user) {
        User savedUser = userService.saveUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @Operation(summary = "Login user", description = "Endpoint to login user")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody UserLogin userLogin) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLogin.getUsername(), userLogin.getPassword()));
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtUtil.generateToken(userLogin.getUsername()));
        loginResponse.setMessage("Welcome, login successful!");
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @Operation(summary = "Get user", description = "Endpoint to get a user by username")
    @GetMapping("/get/{userName}")
    public ResponseEntity<User> getUser(@PathVariable("userName") String userName) {
        User user = userService.getUser(userName);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(summary = "Update user", description = "Endpoint to update the user")
    @PatchMapping("/update/{userName}")
    public ResponseEntity<User> updateUser(@PathVariable("userName") String userName, @Valid @RequestBody UpdateUser updateUser) {
        User user = userService.updateUser(userName, updateUser);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(summary = "Delete user", description = "Endpoint to delete the user")
    @DeleteMapping("/delete/{userName}")
    public ResponseEntity<String> deleteUser(@PathVariable("userName") String userName) {
        String msg = userService.deleteUser(userName);
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @Operation(summary = "Logout user", description = "Endpoint to logout user")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader, Principal principal) {
        String token = authHeader.replace("Bearer ", "");
        jwtUtil.blacklistToken(token);
        return new ResponseEntity<>(principal.getName() + ", you have been logged out.", HttpStatus.OK);
    }
}
