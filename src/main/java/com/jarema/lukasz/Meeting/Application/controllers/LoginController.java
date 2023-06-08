package com.jarema.lukasz.Meeting.Application.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/")
    public String mainPage(){
        return "main-page";
    }

    @GetMapping("/login")
    public String employeeLoginPage() {
        return "login";
    }

    @GetMapping("/welcome")
    public String welcomePage() {
        return "welcome";
    }

    @GetMapping("/access-denied")
    public String accessDeniedPage() {
        return "access-denied";
    }

    @GetMapping("/logout")
    public String logoutPage(HttpServletRequest request) throws ServletException {
        request.logout();
        return "redirect:/";
    }
}
