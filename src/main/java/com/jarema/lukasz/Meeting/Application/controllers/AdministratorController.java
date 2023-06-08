package com.jarema.lukasz.Meeting.Application.controllers;

import com.jarema.lukasz.Meeting.Application.dtos.EmployeeDto;
import com.jarema.lukasz.Meeting.Application.models.Employee;
import com.jarema.lukasz.Meeting.Application.repositories.EmployeeRepository;
import com.jarema.lukasz.Meeting.Application.services.EmailService;
import com.jarema.lukasz.Meeting.Application.services.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class AdministratorController {

    private EmailService emailService;
    private EmployeeRepository employeeRepository;
    private EmployeeService employeeService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AdministratorController(EmailService emailService, EmployeeRepository employeeRepository,
                                   EmployeeService employeeService, PasswordEncoder passwordEncoder) {
        this.emailService = emailService;
        this.employeeRepository = employeeRepository;
        this.employeeService = employeeService;
        this.passwordEncoder = passwordEncoder;
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

    @GetMapping("/admins/changePassword")
    public String administratorChangePasswordForm(Model model) {
        Long employeeId = employeeService.getEmployeeIdByLoggedInInformation();
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        model.addAttribute("employee", employee);
        return "administrator-changePassword";
    }

    @PostMapping("/admins/changePassword")
    public String receptionistSavePassword(@Valid @RequestParam(value = "password") String password,
                                           @ModelAttribute("employee") EmployeeDto employee, BindingResult result,
                                           Model model) {
        if (result.hasErrors()) {
            model.addAttribute("employee", employee);
            return "administrator-changePassword";
        }
        Long employeeId = employeeService.getEmployeeIdByLoggedInInformation();
        employee.setId(employeeId);
        if (password.length() < 6) {
            model.addAttribute("employee", employee);
            return "administrator-changePassword";
        }
        String encodePassword = passwordEncoder.encode(password);
        employeeRepository.updateEmployeePassword(encodePassword, employeeId);
        return "redirect:/admins/home";
    }
}
