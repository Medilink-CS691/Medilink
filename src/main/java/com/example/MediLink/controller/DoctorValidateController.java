package com.example.MediLink.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.MediLink.repository.DoctorAccountRepository;

@RestController
@RequestMapping("/api/DoctorAccounts")
public class DoctorValidateController {

    @Autowired
    private DoctorAccountRepository doctorAccountRepository;

    // Check if the license is unique
    @GetMapping("/check-license")
    public ResponseEntity<Boolean> checkLicenseUnique(@RequestParam String license) {
        boolean exists = doctorAccountRepository.existsByLicense(license);
        return ResponseEntity.ok(exists);  // Return true if license exists, false otherwise
    }

    // Check if the email is unique
    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmailUnique(@RequestParam String email) {
        boolean exists = doctorAccountRepository.existsByEmail(email);
        return ResponseEntity.ok(exists);  // Return true if email exists, false otherwise
    }

    // Check if the phone number is unique
    @GetMapping("/check-phone")
    public ResponseEntity<Boolean> checkPhoneUnique(@RequestParam String phone) {
        boolean exists = doctorAccountRepository.existsByPhone(phone);
        return ResponseEntity.ok(exists);  // Return true if phone number exists, false otherwise
    }
}
