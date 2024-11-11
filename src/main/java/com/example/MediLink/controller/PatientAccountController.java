package com.example.MediLink.controller;

import java.util.List;
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

import com.example.MediLink.model.PatientAccount;
import com.example.MediLink.model.RecordsEntity;
import com.example.MediLink.repository.PatientAccountRepository;
import com.example.MediLink.repository.RecordsRepository;

@Controller
@RequestMapping("/patient")
public class PatientAccountController {

    @Autowired
    private PatientAccountRepository patientAccountRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private RecordsRepository fileRepository;

    // Get patient account details by username
    @GetMapping("/account/username")
    public String getPatientAccountByUsername(@RequestParam("username") String username, Model model) {
        Optional<PatientAccount> patientAccount = patientAccountRepository.findByUsername(username);
        if (patientAccount.isPresent()) {
            model.addAttribute("patientAccount", patientAccount.get());
            return "updatePatientAccount"; // Thymeleaf template to display account details
        } else {
            model.addAttribute("message", "Patient account not found 1.");
            return "error"; // Redirect to an error page or similar
        }
    }

    // Update patient account details
@PostMapping("/account/update")
public String updatePatientAccount(
        @RequestParam String originalUsername,
        @ModelAttribute PatientAccount updatedAccount,
        Model model) {
    
    // Retrieve the existing patient account by original username
    Optional<PatientAccount> existingAccountOpt = patientAccountRepository.findByUsername(originalUsername);
    
    if (existingAccountOpt.isPresent()) {
        PatientAccount existingAccount = existingAccountOpt.get();

        // Update fields with new information from the front end
        existingAccount.setUsername(updatedAccount.getUsername()); // Update username
        existingAccount.setFirstName(updatedAccount.getFirstName());
        existingAccount.setMiddleName(updatedAccount.getMiddleName());
        existingAccount.setLastName(updatedAccount.getLastName());
        existingAccount.setAge(updatedAccount.getAge());
        existingAccount.setGender(updatedAccount.getGender());
        existingAccount.setEmail(updatedAccount.getEmail());
        existingAccount.setPhone(updatedAccount.getPhone());
        existingAccount.setStreet(updatedAccount.getStreet());
        existingAccount.setCity(updatedAccount.getCity());
        existingAccount.setState(updatedAccount.getState());
        existingAccount.setZipCode(updatedAccount.getZipCode());

        // Check if a new password is provided and hash it if so
        String newPassword = updatedAccount.getPassword();
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            String hashedPassword = passwordEncoder.encode(newPassword);
            existingAccount.setPassword(hashedPassword);
        }

        // Save the updated account
        patientAccountRepository.save(existingAccount);


    }
        
        model.addAttribute("username", updatedAccount.getUsername());
        return "patient"; // Redirect to home page on success

}


    // Delete patient account by username
    @PostMapping("/account/delete/{username}")
    public String deletePatientAccount(@PathVariable String username, Model model) {
        Optional<PatientAccount> patientAccount = patientAccountRepository.findByUsername(username);
        if (patientAccount.isPresent()) {
            List<RecordsEntity> files = fileRepository.findByUsername(username);
            if (!files.isEmpty()) {
            fileRepository.deleteAll(files); // Deletes all files associated with the username
        }
            patientAccountRepository.delete(patientAccount.get());
            model.addAttribute("message", "Patient account deleted successfully.");
            return "index2"; // Redirect to a success page
        } else {
            model.addAttribute("message", "Patient account not found 3.");
            return "index2"; // Redirect to an error page or similar
        }
    }
}
