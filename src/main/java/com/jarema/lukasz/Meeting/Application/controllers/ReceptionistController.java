package com.jarema.lukasz.Meeting.Application.controllers;

import com.jarema.lukasz.Meeting.Application.dtos.EmployeeDto;
import com.jarema.lukasz.Meeting.Application.enums.Status;
import com.jarema.lukasz.Meeting.Application.models.Employee;
import com.jarema.lukasz.Meeting.Application.models.Meeting;
import com.jarema.lukasz.Meeting.Application.repositories.EmployeeRepository;
import com.jarema.lukasz.Meeting.Application.repositories.MeetingRepository;
import com.jarema.lukasz.Meeting.Application.services.EmailService;
import com.jarema.lukasz.Meeting.Application.services.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
public class ReceptionistController {

    @Autowired
    public EmployeeService employeeService;
    @Autowired
    public EmployeeRepository employeeRepository;
    @Autowired
    public PasswordEncoder passwordEncoder;
    @Autowired
    public MeetingRepository meetingRepository;
    @Autowired
    public EmailService emailService;

    public ReceptionistController(EmployeeService employeeService, EmployeeRepository employeeRepository,
                                  PasswordEncoder passwordEncoder, MeetingRepository meetingRepository) {
        this.employeeService = employeeService;
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.meetingRepository = meetingRepository;
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

    @GetMapping("/receptionists/myMeetings")
    public String receptionistMyMeetingsPage(Model model, Principal principal) {
        String employeeEmailAddress = principal.getName();
        Employee employee = employeeRepository.findByEmailAddress(employeeEmailAddress);
        model.addAttribute("employee", employee);
        List<Meeting> meetings;
        meetings = employee.getMeeting();
        model.addAttribute("meetings", meetings);
        return "receptionists-myMeetings";
    }

    @PostMapping("/receptionists/myMeetings/search")
    public String searchReceptionistMeetingsByDate(Model model, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                   LocalDate queryDate, Principal principal) {
        String employeeEmailAddress = principal.getName();
        Employee employee = employeeRepository.findByEmailAddress(employeeEmailAddress);
        model.addAttribute("employee", employee);
        LocalDateTime startOfDay = queryDate.atStartOfDay();
        LocalDateTime endOfDay = queryDate.atTime(LocalTime.MAX);
        List<Meeting> meetings = meetingRepository.findByStartOfMeetingBetweenAndEmployees(startOfDay, endOfDay, employee);
        model.addAttribute("meetings", meetings);
        model.addAttribute("queryDate", queryDate);
        return "receptionists-myMeetings";
    }

    @GetMapping("/receptionists/myMeetings/{id}/changeStatus")
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
        return "redirect:/receptionists/home";
    }

    @GetMapping("/receptionists/allMeetings")
    public String receptionistAllMeetings(Model model) {
        List<Meeting> meetings = meetingRepository.findAll();
        model.addAttribute("meetings", meetings);
        return "receptionists-allMeetings";
    }

    @PostMapping("/receptionists/allMeetings/search")
    public String searchMeetingsByDate(Model model, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                       LocalDate queryDate) {
        LocalDateTime startOfDay = queryDate.atStartOfDay();
        LocalDateTime endOfDay = queryDate.atTime(LocalTime.MAX);
        List<Meeting> meetings = meetingRepository.findByStartOfMeetingBetween(startOfDay, endOfDay);
        model.addAttribute("meetings", meetings);
        model.addAttribute("queryDate", queryDate);
        return "receptionists-allMeetings";
    }
}
