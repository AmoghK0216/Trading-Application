package io.github.amoghk0216.trading_backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.amoghk0216.trading_backend.dto.UserDto;
import io.github.amoghk0216.trading_backend.model.User;
import io.github.amoghk0216.trading_backend.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto userDto, HttpServletResponse response){
        logger.debug("Received login request for email: {}", userDto.email());
        String token = authService.login(userDto);
        
        // Set HttpOnly cookie
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // Set to true in production with HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(30 * 60 * 60); // 30 hours
        response.addCookie(cookie);
        
        return new ResponseEntity<>("Login successful", HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserDto userDto, HttpServletResponse response){
        logger.debug("Received signup request for email: {}", userDto.email());
        User user = authService.register(userDto);
        String token = authService.login(userDto);
        
        // Set HttpOnly cookie
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // Set to true in production with HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(30 * 60 * 60); // 30 hours
        response.addCookie(cookie);
        
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response){
        logger.debug("Received logout request");
        
        // Clear the cookie
        Cookie cookie = new Cookie("token", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        
        return new ResponseEntity<>("Logout successful", HttpStatus.OK);
    }
}
