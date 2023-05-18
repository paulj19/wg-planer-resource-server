package com.wgplaner.login;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @GetMapping(value = "/login")
    public ResponseEntity<String> login() {
        //send out the userprofil
        return ResponseEntity.ok("OK");
    }
}