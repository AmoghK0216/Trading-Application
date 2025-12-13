package io.github.amoghk0216.trading_backend.controller;

import io.github.amoghk0216.trading_backend.dto.UserDto;
import io.github.amoghk0216.trading_backend.model.User;
import io.github.amoghk0216.trading_backend.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto userDto){
        logger.debug("Received login request for email: {}", userDto.email());
        String token = authService.login(userDto);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserDto userDto){
        logger.debug("Received signup request for email: {}", userDto.email());
        User user = authService.register(userDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}
