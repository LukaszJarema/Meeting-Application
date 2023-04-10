package com.jarema.lukasz.Meeting.Application.controllers;

import com.jarema.lukasz.Meeting.Application.dtos.EmployeeDto;
import com.jarema.lukasz.Meeting.Application.models.Employee;
import com.jarema.lukasz.Meeting.Application.models.Role;
import com.jarema.lukasz.Meeting.Application.repositories.EmployeeRepository;
import com.jarema.lukasz.Meeting.Application.repositories.RoleRepository;
import com.jarema.lukasz.Meeting.Application.services.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class EmployeeController {
    @Autowired
    public EmployeeService employeeService;

    @Autowired
    public RoleRepository roleRepository;

    @Autowired
    public EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeController(EmployeeService employeeService, RoleRepository roleRepository, EmployeeRepository employeeRepository) {
        this.employeeService = employeeService;
        this.roleRepository = roleRepository;
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/employees")
    public String employessList(Model model) {
        List<EmployeeDto> employees = employeeService.findAllEmployees();
        model.addAttribute("employees", employees);
        return "employees-list";
    }

    @GetMapping("employees/new")
    public String createEmployeeForm(Model model) {
        List<Role> roleList = roleRepository.findAll();
        model.addAttribute("roleList", roleList);
        model.addAttribute("employee", new Employee());
        return "employees-create";
    }

    @PostMapping("employees/new")
    public String saveEmployee(@Valid @ModelAttribute("employee") Employee employee, BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("employee", employee);
            return "employees-create";
        }
        employeeRepository.save(employee);
        return "redirect:/employees";
    }

    @GetMapping("/employees/{employeeId}/edit")
    public String editEmployeeForm(@PathVariable("employeeId") Long employeeId, Model model) {
        EmployeeDto employee = employeeService.findEmployeeById(employeeId);
        model.addAttribute("employee", employee);
        List<Role> roleList = roleRepository.findAll();
        model.addAttribute("roleList", roleList);
        return "employees-edit";
    }

    @PostMapping("/employees/{employeeId}/edit")
    public String updateEmployee(@PathVariable("employeeId") Long employeeId, @Valid @ModelAttribute("employee")
    EmployeeDto employee, BindingResult result) {
        if (result.hasErrors()) {
            return "employees-edit";
        }
        employee.setId(employeeId);
        employeeService.updateEmployee(employee);
        return "redirect:/employees";
    }

    @GetMapping("/employees/{employeeId}/changePassword")
    public String editEmployeePassword(@PathVariable ("employeeId") Long employeeId, Model model) {
        EmployeeDto employee = employeeService.findEmployeeById(employeeId);
        model.addAttribute("employee", employee);
        List<Role> roleList = roleRepository.findAll();
        model.addAttribute("roleList", roleList);
        return "employees-changePassword";
    }

    @PostMapping("/employees/{employeeId}/changePassword")
    public String updateEmployeePassword(@PathVariable("employeeId") Long employeeId, @Valid
    @RequestParam(value = "password") String password, BindingResult result) {
        if (result.hasErrors()) {
            return "employees-changePassword";
        }
        employeeService.updateEmployeePassword(password, employeeId);
        return "redirect:/employees";
    }

    @GetMapping("/employees/{employeeId}/delete")
    public String deleteEmployee(@PathVariable("employeeId") Long employeeId) {
        employeeService.delete(employeeId);
        return "redirect:/employees";
    }

    @GetMapping("/employees/search")
    public String searchEmployeesByNameOrSurname(@RequestParam(value = "query") String query, Model model) {
        List<EmployeeDto> employees = employeeService.searchEmployeesByNameOrSurname(query);
        model.addAttribute("employees", employees);
        return "employees-list";
    }
}
