package com.jarema.lukasz.Meeting.Application.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/loginAsAnAdmin")
    public String loginAsAnAdministratorPage() {
        return "administrators-login";
    }

    @GetMapping("/loginAsAnEmployee")
    public String loginAsAnEmployeePage() {
        return "employees-login";
    }

    @GetMapping("/administrator/home")
    public String viewAdminHomePage() {
        return "administrator-home";
    }

    @GetMapping("/loginAsAReceptionist")
    public String loginAsAReceptionistPage() {
        return "receptionists-login";
    }

    @GetMapping("/loginAsAVisitor")
    public String loginAsAVisitorPage() {
        return "visitors-login";
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
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        request.logout();
        return "redirect:/";
    }
}
