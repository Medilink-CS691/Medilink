package com.example.MediLink.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.MediLink.model.DoctorAccount;
import com.example.MediLink.repository.DoctorAccountRepository;

@Service
public class DoctorAccountService {

    @Autowired
    private DoctorAccountRepository doctorAccountRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder; // Add this line

    public void saveAccount(DoctorAccount doctorAccount) {
        // Hash the password before saving
        String hashedPassword = passwordEncoder.encode(doctorAccount.getPassword());
        doctorAccount.setPassword(hashedPassword); // Set the hashed password
        doctorAccountRepository.save(doctorAccount);
    }

    public boolean authenticate(String license, String password) {
        Optional<DoctorAccount> accountOptional = doctorAccountRepository.findByLicense(license);
        
        if (accountOptional.isPresent()) {
            DoctorAccount account = accountOptional.get();
            // Check if the provided password matches the stored hashed password
            return passwordEncoder.matches(password, account.getPassword());
        }

        return false; // License not found
    }
}
