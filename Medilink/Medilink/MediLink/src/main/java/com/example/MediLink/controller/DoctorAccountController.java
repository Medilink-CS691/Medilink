package com.example.MediLink.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.MediLink.model.DoctorAccount;
import com.example.MediLink.repository.DoctorAccountRepository;

@Controller
@RequestMapping("/doctor")
public class DoctorAccountController {

    @Autowired
    private DoctorAccountRepository doctorAccountRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Get doctor account details by license
    @GetMapping("/account/license")
    public String getDoctorAccountByLicense(@RequestParam String license, Model model) {
        Optional<DoctorAccount> doctorAccount = doctorAccountRepository.findByLicense(license);
        if (doctorAccount.isPresent()) {
            model.addAttribute("doctorAccount", doctorAccount.get());
            return "updateDoctorAccount"; // Thymeleaf template to display account details
        } else {
            model.addAttribute("message", "Doctor account not found.");
            return "error"; // Redirect to an error page or similar
        }
    }

    // Update doctor account details
    @PostMapping("/account/update")
    public String updateDoctorAccount(
        @RequestParam String originalLicense,
        @ModelAttribute DoctorAccount updatedAccount, 
        Model model) {

        Optional<DoctorAccount> existingAccount = doctorAccountRepository.findByLicense(originalLicense);
        if (existingAccount.isPresent()) {
            DoctorAccount account = existingAccount.get();
            // Update fields with new information
            account.setLicense(updatedAccount.getLicense());
            account.setFullName(updatedAccount.getFullName());
            account.setEmail(updatedAccount.getEmail());
            account.setPhone(updatedAccount.getPhone());
            account.setSpecialization(updatedAccount.getSpecialization());

            String newPassword = updatedAccount.getPassword();
            if (newPassword != null && !newPassword.trim().isEmpty()) {
                String hashedPassword = passwordEncoder.encode(newPassword);
                account.setPassword(hashedPassword);
            }
    
            // Save the updated account
            doctorAccountRepository.save(account);
    
    
        }
            model.addAttribute("license", updatedAccount.getLicense());
            return "doctor"; // Redirect to home page on success
    }

    // Delete doctor account by license
    @PostMapping("/account/delete/{license}")
    public String deleteDoctorAccount(@PathVariable String license, Model model) {
        Optional<DoctorAccount> doctorAccount = doctorAccountRepository.findByLicense(license);
        if (doctorAccount.isPresent()) {
            doctorAccountRepository.delete(doctorAccount.get());
            model.addAttribute("message", "Doctor account deleted successfully.");
            return "index2"; // Redirect to a success page
        } else {
            model.addAttribute("message", "Doctor account not found.");
            return "index2"; // Redirect to an error page or similar
        }
    }
}
