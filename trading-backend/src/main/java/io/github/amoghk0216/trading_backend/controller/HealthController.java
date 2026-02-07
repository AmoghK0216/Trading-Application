package io.github.amoghk0216.trading_backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<String> heatlh(){
        return new ResponseEntity<>("Welcome to the Application", HttpStatus.OK);
    }
}
