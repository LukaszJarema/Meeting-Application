package com.jarema.lukasz.Meeting.Application.controllers;

import com.jarema.lukasz.Meeting.Application.models.Employee;
import com.jarema.lukasz.Meeting.Application.repositories.EmployeeRepository;
import com.jarema.lukasz.Meeting.Application.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class ReceptionistController {

    @Autowired
    public EmployeeService employeeService;
    @Autowired
    public EmployeeRepository employeeRepository;

    public ReceptionistController(EmployeeService employeeService, EmployeeRepository employeeRepository) {
        this.employeeService = employeeService;
        this.employeeRepository = employeeRepository;
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
}
