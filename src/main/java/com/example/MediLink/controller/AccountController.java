package com.example.MediLink.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.MediLink.model.PatientAccount;
import com.example.MediLink.repository.AccountRepository;
import com.example.MediLink.service.AccountStorageService;

@Controller
public class AccountController {
    
    @Autowired
    private AccountStorageService accountStorageService;

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/login.html")
    public String returnLogin() {

        return "login";

    }

    @GetMapping("/contact.html")
    public String returnContact() {

        return "contact";

    }

    @GetMapping("/signup.html")
    public String returnsignup() {

        return "signup";

    }

    @GetMapping("/index.html")
    public String returnindex() {

        return "index2";

    }

    @PostMapping("/Account/Sign_up")
    public String signUp(@RequestParam String username,@RequestParam String firstName,String middleName,@RequestParam String lastName,
    @RequestParam String age,@RequestParam String gender,@RequestParam String email,@RequestParam String phone,@RequestParam String password,@RequestParam String street, 
    @RequestParam String city,@RequestParam String state,@RequestParam String zipCode) {
    
        PatientAccount account=new PatientAccount(username,firstName,middleName,lastName,
        age,gender,email,phone,password,street,city,state,zipCode);
        account.setMiddleName(middleName);
        accountStorageService.saveAccount(account);
        return "login";
    }

    @PostMapping("/Account/log_in")
    public String login(@RequestParam("username") String username,
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
}
