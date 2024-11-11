package com.example.MediLink.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.MediLink.service.EmailService;

@Controller
@RequestMapping("/api/contact")
public class ContactController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/returncontact")
    public String returncontact(@RequestParam("username") String username, Model model) {
        model.addAttribute("username", username);
        return "contact"; // Ensure this template exists
    }

    @PostMapping("/send")
    public String sendContactMessage(@RequestParam String name,
                                     @RequestParam String email,
                                     @RequestParam String subject,
                                     @RequestParam String message, Model model) {
        emailService.sendContactEmail(name, email, subject, message);
        model.addAttribute("username", name);
        return "patient";
    }
}

