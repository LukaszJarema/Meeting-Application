package com.jarema.lukasz.Meeting.Application.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdministratorController {

    @GetMapping("admin/home")
    public String getAdminHome() {
        return "administrator-home";
    }
}
