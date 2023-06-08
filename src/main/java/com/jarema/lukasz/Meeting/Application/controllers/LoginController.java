package com.jarema.lukasz.Meeting.Application.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @GetMapping("/")
    public String mainPage(){
        return "main-page";
    }

    @PostMapping("/")
    public String mainPagePost() {
        return "main-page";
    }

    @GetMapping("/login")
    public String employeeLoginPage() {
        return "login";
    }

    @GetMapping("/access-denied")
    public String accessDeniedPage() {
        return "access-denied";
    }

    @GetMapping("/accountDisabled")
    public String accountDisabledPage() {
        return "account-disabled";
    }

    @GetMapping("/logout")
    public String logoutPage(HttpServletRequest request) throws ServletException {
        request.logout();
        return "redirect:/";
    }
}
