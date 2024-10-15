package com.example.MediLink.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.MediLink.model.Account;
import com.example.MediLink.repository.AccountRepository;

@Service
public class AccountStorageService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Method to save a new account
    public void saveAccount(Account account) {
        // Hash the password before saving
        String hashedPassword = passwordEncoder.encode(account.getPassword());
        account.setPassword(hashedPassword);
        accountRepository.save(account);
    }

    // Method to authenticate user
    public boolean authenticate(String username, String password) {
        Optional<Account> accountOptional = accountRepository.findByUsername(username);
        
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            // Check if the provided password matches the stored hashed password
            return passwordEncoder.matches(password, account.getPassword());
        }
        
        return false; // Username not found
    }
}