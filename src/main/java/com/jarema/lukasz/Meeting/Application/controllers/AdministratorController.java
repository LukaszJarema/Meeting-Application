package com.jarema.lukasz.Meeting.Application.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdministratorController {

    @GetMapping("/admins/login")
    public String loginAsAnAdministratorPage() {
        return "administrators-login";
    }
}
