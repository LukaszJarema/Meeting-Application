package com.jarema.lukasz.Meeting.Application.controllers;

import com.jarema.lukasz.Meeting.Application.models.Employee;
import com.jarema.lukasz.Meeting.Application.repositories.EmployeeRepository;
import com.jarema.lukasz.Meeting.Application.services.EmailService;
import com.jarema.lukasz.Meeting.Application.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class AdministratorController {

    private EmailService emailService;
    private EmployeeRepository employeeRepository;
    private EmployeeService employeeService;

    @Autowired
    public AdministratorController(EmailService emailService, EmployeeRepository employeeRepository,
                                   EmployeeService employeeService) {
        this.emailService = emailService;
        this.employeeRepository = employeeRepository;
        this.employeeService = employeeService;
    }

    @GetMapping("/admins/home")
    public String getAdminHome() {
        return "administrator-home";
    }

    @GetMapping("/admins/accountDetails")
    public String viewAdministratorDetails(Model model) {
        Long employeeId = employeeService.getEmployeeIdByLoggedInInformation();
        Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);
        Employee employee = employeeOptional.orElseThrow(() -> new IllegalArgumentException("Employee not found"));
        model.addAttribute("employee", employee);
        return "administrator-accountDetails";
    }

    @PostMapping("/admins/accountDetails")
    public String viewAdministratorDetailsForm() {
        return "redirect:/admins/home";
    }
}
