package com.example.MediLink.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.MediLink.model.DoctorAccount;
import com.example.MediLink.model.PatientAccount;
import com.example.MediLink.service.DoctorAccountService;
import com.example.MediLink.service.PatientAccountService;

@Controller
public class HomepageLoginSignupController {
    
    @Autowired
    private PatientAccountService accountStorageService;

    @Autowired
    private DoctorAccountService doctorAccountService;

    @GetMapping("/login.html")
    public String returnLogin() {

        return "login";

    }

    @GetMapping("/contact.html")
    public String returnContact() {

        return "contact";

    }

    @GetMapping("/Patientsignup.html")
    public String returnPatientsignup() {

        return "Patientsignup";

    }

    @GetMapping("/Doctorsignup.html")
    public String returnDoctorsignup() {

        return "Doctorsignup";

    }

    @GetMapping("/index.html")
    public String returnindex() {

        return "index2";

    }

    @PostMapping("/PatientAccount/Sign_up")
    public String PatientsignUp(@RequestParam String username,@RequestParam String firstName,String middleName,@RequestParam String lastName,
    @RequestParam String age,@RequestParam String gender,@RequestParam String email,@RequestParam String phone,@RequestParam String password,@RequestParam String street, 
    @RequestParam String city,@RequestParam String state,@RequestParam String zipCode) {
    
        PatientAccount account=new PatientAccount(username,firstName,middleName,lastName,
        age,gender,email,phone,password,street,city,state,zipCode);
        account.setMiddleName(middleName);
        accountStorageService.saveAccount(account);
        return "login";
    }

    @PostMapping("/PatientAccount/log_in")
    public String Patientlogin(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        Model model) {
        boolean isAuthenticated = accountStorageService.authenticate(username, password);
        
        if (isAuthenticated) {
            model.addAttribute("username", username);
            return "patient"; // Redirect to home page on success
        } else {
            model.addAttribute("errorMessage", "Can't find an account with that username and password.");
            return "login"; // Return to login page with an error message
        }
    }

    @PostMapping("/DoctorAccount/Sign_up")
    public String DoctorsignUp(@RequestParam String fullName, @RequestParam String email,
                         @RequestParam String phone, @RequestParam String license,
                         @RequestParam String specialization, @RequestParam String password) {

        DoctorAccount doctorAccount = new DoctorAccount(fullName, email, phone, license, specialization, password);
        doctorAccountService.saveAccount(doctorAccount);
        return "login";
    }

    // Login endpoint for DoctorAccount using license and password
    @PostMapping("/DoctorAccount/log_in")
    public String Doctorlogin(@RequestParam("license") String license,
                        @RequestParam("password") String password,
                        Model model) {

        boolean isAuthenticated = doctorAccountService.authenticate(license, password);

        if (isAuthenticated) {
            model.addAttribute("license", license);
            return "doctor"; // Redirect to doctor home page on success
        } else {
            model.addAttribute("errorMessage", "Invalid license or password.");
            return "login"; // Return to login page with an error message
        }
    }
}
