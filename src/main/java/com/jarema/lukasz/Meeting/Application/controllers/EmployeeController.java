package com.jarema.lukasz.Meeting.Application.controllers;

import com.jarema.lukasz.Meeting.Application.dtos.EmployeeDto;
import com.jarema.lukasz.Meeting.Application.models.Employee;
import com.jarema.lukasz.Meeting.Application.models.Role;
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
    public EmployeeService employeeService;

    @Autowired
    public RoleRepository roleRepository;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/employees")
    public String employessList(Model model) {
        List<EmployeeDto> employees = employeeService.findAllEmployees();
        model.addAttribute("employees", employees);
        return "employees-list";
    }

    @GetMapping("employees/new")
    public String createEmployeeForm(Model model) {
        Employee employee = new Employee();
        model.addAttribute("employee", employee);
        List<Role> roleList = roleRepository.findAll();
        model.addAttribute("roleList", roleList);
        return "employees-create";
    }

    @PostMapping("/employees/new")
    public String saveEmployee(@Valid @ModelAttribute("employee") EmployeeDto employeeDto, BindingResult result,
                               Model model) {
        if (result.hasErrors()) {
            model.addAttribute("employee", employeeDto);
            return "employees-create";
        }
        employeeService.saveEmployee(employeeDto);
        return "redirect:/employees";
    }

    @GetMapping("/employees/{employeeId}/edit")
    public String editEmployeeForm(@PathVariable("employeeId") Long employeeId, Model model) {
        EmployeeDto employee = employeeService.findEmployeeById(employeeId);
        model.addAttribute("employee", employee);
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
