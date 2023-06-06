package com.jarema.lukasz.Meeting.Application.controllers;

import com.jarema.lukasz.Meeting.Application.dtos.EmployeeDto;
import com.jarema.lukasz.Meeting.Application.models.Employee;
import com.jarema.lukasz.Meeting.Application.models.Meeting;
import com.jarema.lukasz.Meeting.Application.models.Role;
import com.jarema.lukasz.Meeting.Application.repositories.EmployeeRepository;
import com.jarema.lukasz.Meeting.Application.repositories.MeetingRepository;
import com.jarema.lukasz.Meeting.Application.repositories.RoleRepository;
import com.jarema.lukasz.Meeting.Application.services.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Controller
public class EmployeeController {
    @Autowired
    public EmployeeService employeeService;

    @Autowired
    public RoleRepository roleRepository;

    @Autowired
    public EmployeeRepository employeeRepository;

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Autowired
    public MeetingRepository meetingRepository;

    @Autowired
    public EmployeeController(EmployeeService employeeService, RoleRepository roleRepository,
                              EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder,
                              MeetingRepository meetingRepository) {
        this.employeeService = employeeService;
        this.roleRepository = roleRepository;
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.meetingRepository = meetingRepository;
    }

    @GetMapping("/employees/home")
    public String viewEmployeeHomePage() {
        return "employees-home";
    }

    @GetMapping("/employees/accountDetails")
    public String viewEmployeeDetails(Model model) {
        Long employeeId = employeeService.getVisitorIdByLoggedInInformation();
        Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);
        Employee employee = employeeOptional.orElseThrow(() -> new IllegalArgumentException("Employee not found"));
        model.addAttribute("employee", employee);
        return "employees-accountDetails";
    }

    @PostMapping("/employees/accountDetails")
    public String viewEmployeeDetailsForm() {
        return "redirect:/employees/welcome";
    }

    @GetMapping("/employees/changePassword")
    public String employeeChangePasswordForm(Model model) {
        Long employeeId = employeeService.getVisitorIdByLoggedInInformation();
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        model.addAttribute("employee", employee);
        return "employees-changePassword";
    }

    @PostMapping("/employees/changePassword")
    public String employeeSavePassword(@Valid @RequestParam(value = "password") String password,
                                        @ModelAttribute("employee") EmployeeDto employee, BindingResult result,
                                        Model model) {
        if (result.hasErrors()) {
            model.addAttribute("employee", employee);
            return "employees-changePassword";
        }
        Long employeeId = employeeService.getVisitorIdByLoggedInInformation();
        employee.setId(employeeId);
        String encodePassword = passwordEncoder.encode(password);
        employeeRepository.updateEmployeePassword(encodePassword, employeeId);
        return "redirect:/employees/home";
    }

    @GetMapping("/employees/myMeetings")
    public String employeeMyMeetingsPage(Model model, Principal principal) {
        String employeeEmailAddress = principal.getName();
        Employee employee = employeeRepository.findByEmailAddress(employeeEmailAddress);
        model.addAttribute("employee", employee);
        List<Meeting> meetings;
        meetings = employee.getMeeting();
        model.addAttribute("meetings", meetings);
        return "employees-myMeetings";
    }

    @PostMapping("/employees/myMeetings/search")
    public String searchEmployeeMeetingsByDate(Model model, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                               LocalDate queryDate, Principal principal) {
        String employeeEmailAddress = principal.getName();
        Employee employee = employeeRepository.findByEmailAddress(employeeEmailAddress);
        model.addAttribute("employee", employee);
        LocalDateTime startOfDay = queryDate.atStartOfDay();
        LocalDateTime endOfDay = queryDate.atTime(LocalTime.MAX);
        List<Meeting> meetings = meetingRepository.findByStartOfMeetingBetweenAndEmployees(startOfDay, endOfDay, employee);
        model.addAttribute("meetings", meetings);
        model.addAttribute("queryDate", queryDate);
        return "employees-myMeetings";
    }

    @GetMapping("/employees/list")
    public String employeesList(Model model) {
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
    public String saveEmployee(@Valid @ModelAttribute("employee") EmployeeDto employeeDto, BindingResult result,
                              Model model) {
        Employee exsistingEmployeeEmailAddress = employeeService.findByEmail(employeeDto.getEmailAddress());
        if(exsistingEmployeeEmailAddress != null && exsistingEmployeeEmailAddress.getEmailAddress() != null && !exsistingEmployeeEmailAddress.getEmailAddress().isEmpty()) {
            result.rejectValue("emailAddress", "error.emailAddress", "There is already a Visitor with this email address or username");
        }
        if(result.hasErrors()) {
            model.addAttribute("employee", employeeDto);
            return "employees-create";
        }
        employeeService.saveEmployee(employeeDto);
        return "redirect:/logout";
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

    /*
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
    @RequestParam(value = "password") String password, EmployeeDto employeeDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "employees-changePassword";
        }
        if (password.length() < 6) {
            employeeDto.setId(employeeId);
            model.addAttribute("employee", employeeDto);
            return "employees-changePassword";
        }
        employeeDto.setId(employeeId);
        employeeRepository.updateEmployeePassword(password, employeeId);
        return "redirect:/employees";
    }

     */

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
