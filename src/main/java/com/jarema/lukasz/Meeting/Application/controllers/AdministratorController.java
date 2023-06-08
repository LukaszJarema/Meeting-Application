package com.jarema.lukasz.Meeting.Application.controllers;

import com.jarema.lukasz.Meeting.Application.dtos.EmployeeDto;
import com.jarema.lukasz.Meeting.Application.enums.Status;
import com.jarema.lukasz.Meeting.Application.models.Employee;
import com.jarema.lukasz.Meeting.Application.models.Meeting;
import com.jarema.lukasz.Meeting.Application.models.Role;
import com.jarema.lukasz.Meeting.Application.repositories.EmployeeRepository;
import com.jarema.lukasz.Meeting.Application.repositories.MeetingRepository;
import com.jarema.lukasz.Meeting.Application.repositories.RoleRepository;
import com.jarema.lukasz.Meeting.Application.services.EmailService;
import com.jarema.lukasz.Meeting.Application.services.EmployeeService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Controller
public class AdministratorController {

    private EmailService emailService;
    private EmployeeRepository employeeRepository;
    private EmployeeService employeeService;
    private PasswordEncoder passwordEncoder;
    private MeetingRepository meetingRepository;
    private RoleRepository roleRepository;

    @Autowired
    public AdministratorController(EmailService emailService, EmployeeRepository employeeRepository,
                                   EmployeeService employeeService, PasswordEncoder passwordEncoder,
                                   MeetingRepository meetingRepository, RoleRepository roleRepository) {
        this.emailService = emailService;
        this.employeeRepository = employeeRepository;
        this.employeeService = employeeService;
        this.passwordEncoder = passwordEncoder;
        this.meetingRepository = meetingRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/admins/home")
    public String getAdminHome() {
        return "administrators-home";
    }

    @GetMapping("/admins/accountDetails")
    public String viewAdministratorDetails(Model model) {
        Long employeeId = employeeService.getEmployeeIdByLoggedInInformation();
        Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);
        Employee employee = employeeOptional.orElseThrow(() -> new IllegalArgumentException("Employee not found"));
        model.addAttribute("employee", employee);
        return "administrators-accountDetails";
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
        return "administrators-changePassword";
    }

    @PostMapping("/admins/changePassword")
    public String receptionistSavePassword(@Valid @RequestParam(value = "password") String password,
                                           @ModelAttribute("employee") EmployeeDto employee, BindingResult result,
                                           Model model) {
        if (result.hasErrors()) {
            model.addAttribute("employee", employee);
            return "administrators-changePassword";
        }
        Long employeeId = employeeService.getEmployeeIdByLoggedInInformation();
        employee.setId(employeeId);
        if (password.length() < 6) {
            model.addAttribute("employee", employee);
            return "administrators-changePassword";
        }
        String encodePassword = passwordEncoder.encode(password);
        employeeRepository.updateEmployeePassword(encodePassword, employeeId);
        return "redirect:/admins/home";
    }

    @GetMapping("admins/myMeetings")
    public String receptionistMyMeetingsPage(Model model, Principal principal) {
        String employeeEmailAddress = principal.getName();
        Employee employee = employeeRepository.findByEmailAddress(employeeEmailAddress);
        model.addAttribute("employee", employee);
        List<Meeting> meetings;
        meetings = employee.getMeeting();
        meetings.sort(Comparator.comparing(Meeting::getStartOfMeeting).reversed());
        model.addAttribute("meetings", meetings);
        return "administrators-myMeetings";
    }

    @PostMapping("/admins/myMeetings/search")
    public String searchReceptionistMeetingsByDate(Model model, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate queryDate, Principal principal) {
        String employeeEmailAddress = principal.getName();
        Employee employee = employeeRepository.findByEmailAddress(employeeEmailAddress);
        model.addAttribute("employee", employee);
        LocalDateTime startOfDay = queryDate.atStartOfDay();
        LocalDateTime endOfDay = queryDate.atTime(LocalTime.MAX);
        Sort sort = Sort.by(Sort.Direction.DESC, "startOfMeeting");
        List<Meeting> meetings = meetingRepository.findByStartOfMeetingBetweenAndEmployees(startOfDay, endOfDay,
                employee, sort);
        model.addAttribute("meetings", meetings);
        model.addAttribute("queryDate", queryDate);
        return "administrators-myMeetings";
    }

    @GetMapping("/admins/myMeetings/{id}/changeStatus")
    @Transactional
    public String changeMeetingStatus(@PathVariable Long id) {
        Optional<Meeting> meeting = meetingRepository.findById(id);
        String content = meeting.get().getContentOfMeeting();
        Long employeeId = employeeService.getEmployeeIdByLoggedInInformation();
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        String employeeNameAndSurname = employee.get().getName() + " " + employee.get().getSurname();
        String status = "";
        Status stat;
        List<Employee> employees = meeting.get().getEmployees();
        if (meeting.get().getStatus() == Status.REJECTED) {
            stat = Status.APPROVED;
            status = "APPROVED";
        }
        else {
            stat = Status.REJECTED;
            status = "REJECTED";
        }
        for (Employee employee1 : employees) {
            emailService.sendConfirmationAboutChangedStatusOfMeeting(employee1.getEmailAddress(), employeeNameAndSurname,
                    content, status);
        }
        emailService.sendConfirmationAboutChangedStatusOfMeeting(meeting.get().getVisitor().getEmailAddress(),
                employeeNameAndSurname, content, status);
        meetingRepository.updateMeetingStatus(stat, id);
        return "redirect:/admins/home";
    }

    @GetMapping("/admins/allMeetings")
    public String administratorAllMeetings(Model model) {
        List<Meeting> meetings = meetingRepository.findAllMeetingsSortedByStartDateDescending();
        model.addAttribute("meetings", meetings);
        return "administrators-allMeetings";
    }

    @PostMapping("/admins/allMeetings/search")
    public String searchMeetingsByDate(Model model, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate queryDate) {
        LocalDateTime startOfDay = queryDate.atStartOfDay();
        LocalDateTime endOfDay = queryDate.atTime(LocalTime.MAX);
        Sort sort = Sort.by(Sort.Direction.DESC, "startOfMeeting");
        List<Meeting> meetings = meetingRepository.findByStartOfMeetingBetween(startOfDay, endOfDay, sort);
        model.addAttribute("meetings", meetings);
        model.addAttribute("queryDate", queryDate);
        return "administrators-allMeetings";
    }

    @GetMapping("/admins/employees/new")
    public String createEmployeeForm(Model model) {
        List<Role> roleList = roleRepository.findAll();
        model.addAttribute("roleList", roleList);
        model.addAttribute("employee", new Employee());
        return "administrators-createEmployee";
    }

    @PostMapping("/admins/employees/new")
    public String saveEmployee(@Valid @ModelAttribute("employee") EmployeeDto employeeDto, BindingResult result,
                               Model model) {
        Employee exsistingEmployeeEmailAddress = employeeService.findByEmail(employeeDto.getEmailAddress());
        if(exsistingEmployeeEmailAddress != null && exsistingEmployeeEmailAddress.getEmailAddress() != null && !exsistingEmployeeEmailAddress.getEmailAddress().isEmpty()) {
            result.rejectValue("emailAddress", "error.emailAddress", "There is already a Visitor with this email address or username");
        }
        if(result.hasErrors()) {
            model.addAttribute("employee", employeeDto);
            return "administrators-createEmployee";
        }
        employeeService.saveEmployee(employeeDto);
        return "redirect:/admins/home";
    }

    @GetMapping("/admins/employees")
    public String employeesList(Model model) {
        List<Employee> employees = employeeRepository.findAllEmployeesSortedBySurnameAscending();
        model.addAttribute("employees", employees);
        return "administrators-allEmployees";
    }

    @GetMapping("/admins/employees/search")
    public String searchEmployeesByNameOrSurname(@RequestParam(value = "query") String query, Model model) {
        List<EmployeeDto> employees = employeeService.searchEmployeesByNameOrSurname(query);
        model.addAttribute("employees", employees);
        return "administrators-allEmployees";
    }

    @GetMapping("/admins/employees/{employeeId}/edit")
    public String editEmployeeForm(@PathVariable("employeeId") Long employeeId, Model model) {
        EmployeeDto employee = employeeService.findEmployeeById(employeeId);
        model.addAttribute("employee", employee);
        List<Role> roleList = roleRepository.findAll();
        model.addAttribute("roleList", roleList);
        return "administrators-employeesEdit";
    }

    @PostMapping("/admins/employees/{employeeId}/edit")
    public String updateEmployee(@PathVariable("employeeId") Long employeeId, @Valid @ModelAttribute("employee")
                                 EmployeeDto employee, BindingResult result) {
        if (result.hasErrors()) {
            return "administrators-employeesEdit";
        }
        employee.setId(employeeId);
        employeeService.updateEmployee(employee);
        return "redirect:/admins/home";
    }
}
