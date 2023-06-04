package com.jarema.lukasz.Meeting.Application.controllers;

import com.jarema.lukasz.Meeting.Application.services.EmailService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdministratorController {

    private EmailService emailService;

    public AdministratorController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("admin/home")
    public String getAdminHome() {
        return "administrator-home";
    }
}
