package com.jarema.lukasz.Meeting.Application.controllers;

import com.jarema.lukasz.Meeting.Application.dtos.SupportDto;
import com.jarema.lukasz.Meeting.Application.models.Support;
import com.jarema.lukasz.Meeting.Application.repositories.EmployeeRepository;
import com.jarema.lukasz.Meeting.Application.services.EmailService;
import com.jarema.lukasz.Meeting.Application.services.SupportService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class LoginController {

    private SupportService supportService;
    private EmployeeRepository employeeRepository;
    private EmailService emailService;

    @Autowired
    public LoginController(SupportService supportService, EmployeeRepository employeeRepository,
                           EmailService emailService) {
        this.supportService = supportService;
        this.employeeRepository = employeeRepository;
        this.emailService = emailService;
    }

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

    @GetMapping("/support")
    public String supportForm(Model model) {
        Support support = new Support();
        model.addAttribute("support", support);
        return "support";
    }

    @PostMapping("/support")
    public String supportSave(@Valid @ModelAttribute("support") SupportDto supportDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("support", supportDto);
            return "support";
        }
        List<String> administratorsEmailAddresses = employeeRepository.findAllByRoleAdministrator();
        for (int i = 0; i < administratorsEmailAddresses.size(); i++) {
            emailService.sendInformationAboutNewTicketToAdmins(administratorsEmailAddresses.get(i));
        }
        supportService.saveSupport(supportDto);
        return "redirect:/";
    }
}
