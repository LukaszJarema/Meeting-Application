package com.jarema.lukasz.Meeting.Application.controllers;

import com.jarema.lukasz.Meeting.Application.dtos.EmployeeDto;
import com.jarema.lukasz.Meeting.Application.models.Employee;
import com.jarema.lukasz.Meeting.Application.repositories.EmployeeRepository;
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
public class ReceptionistController {

    @Autowired
    public EmployeeService employeeService;
    @Autowired
    public EmployeeRepository employeeRepository;
    @Autowired
    public PasswordEncoder passwordEncoder;

    public ReceptionistController(EmployeeService employeeService, EmployeeRepository employeeRepository,
                                  PasswordEncoder passwordEncoder) {
        this.employeeService = employeeService;
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/receptionists/home")
    public String getAdminHome() {
        return "receptionist-home";
    }

    @GetMapping("/receptionists/accountDetails")
    public String viewReceptionistDetails(Model model) {
        Long employeeId = employeeService.getEmployeeIdByLoggedInInformation();
        Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);
        Employee employee = employeeOptional.orElseThrow(() -> new IllegalArgumentException("Employee not found"));
        model.addAttribute("employee", employee);
        return "receptionist-accountDetails";
    }

    @PostMapping("/receptionists/accountDetails")
    public String viewReceptionistDetailsForm() {
        return "redirect:/receptionists/home";
    }

    @GetMapping("/receptionists/changePassword")
    public String receptionistChangePasswordForm(Model model) {
        Long employeeId = employeeService.getEmployeeIdByLoggedInInformation();
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        model.addAttribute("employee", employee);
        return "receptionists-changePassword";
    }

    @PostMapping("/receptionists/changePassword")
    public String receptionistSavePassword(@Valid @RequestParam(value = "password") String password,
                                           @ModelAttribute("employee")EmployeeDto employee, BindingResult result,
                                           Model model) {
        if (result.hasErrors()) {
            model.addAttribute("employee", employee);
            return "receptionists-changePassword";
        }
        Long employeeId = employeeService.getEmployeeIdByLoggedInInformation();
        employee.setId(employeeId);
        String encodePassword = passwordEncoder.encode(password);
        employeeRepository.updateEmployeePassword(encodePassword, employeeId);
        return "redirect:/receptionists/home";
    }
}
