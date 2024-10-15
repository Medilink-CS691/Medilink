package com.example.MediLink.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.MediLink.repository.AccountRepository;

@RestController
@RequestMapping("/api/accounts")
public class UserNameController {

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsernameUnique(@RequestParam String username) {
        boolean exists = accountRepository.findByUsername(username).isPresent();
        return ResponseEntity.ok(exists);  // Return true if username exists, false otherwise
    }
}

