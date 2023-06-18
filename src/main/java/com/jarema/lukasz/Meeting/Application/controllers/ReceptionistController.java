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
import org.springframework.data.domain.Sort;
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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Controller
public class ReceptionistController {

    private EmployeeService employeeService;
    private EmployeeRepository employeeRepository;
    private PasswordEncoder passwordEncoder;
    private MeetingRepository meetingRepository;
    private EmailService emailService;

    public ReceptionistController(EmployeeService employeeService, EmployeeRepository employeeRepository,
                                  PasswordEncoder passwordEncoder, MeetingRepository meetingRepository,
                                  EmailService emailService) {
        this.employeeService = employeeService;
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.meetingRepository = meetingRepository;
        this.emailService = emailService;
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
        if (password.length() < 6) {
            model.addAttribute("employee", employee);
            return "receptionists-changePassword";
        }
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
        meetings.sort(Comparator.comparing(Meeting::getStartOfMeeting).reversed());
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
        Sort sort = Sort.by(Sort.Direction.DESC, "startOfMeeting");
        List<Meeting> meetings = meetingRepository.findByStartOfMeetingBetweenAndEmployees(startOfDay, endOfDay,
                employee, sort);
        model.addAttribute("meetings", meetings);
        model.addAttribute("queryDate", queryDate);
        return "receptionists-myMeetings";
    }

    @GetMapping("/receptionists/myMeetings/{id}/accept")
    @Transactional
    public String acceptMeeting(@PathVariable Long id) {
        Optional<Meeting> meeting = meetingRepository.findById(id);
        String content = meeting.get().getContentOfMeeting();
        Long employeeId = employeeService.getEmployeeIdByLoggedInInformation();
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        String employeeNameAndSurname = employee.get().getName() + " " + employee.get().getSurname();
        String status = "APPROVED";
        Status stat = Status.APPROVED;
        List<Employee> employees = meeting.get().getEmployees();
        for (Employee employee1 : employees) {
            emailService.sendConfirmationAboutChangedStatusOfMeeting(employee1.getEmailAddress(), employeeNameAndSurname,
                    content, status);
        }
        emailService.sendConfirmationAboutChangedStatusOfMeeting(meeting.get().getVisitor().getEmailAddress(),
                employeeNameAndSurname, content, status);
        meetingRepository.updateMeetingStatus(stat, id);
        return "redirect:/receptionists/home";
    }

    @GetMapping("/receptionists/myMeetings/{id}/reject")
    @Transactional
    public String rejectMeeting(@PathVariable Long id) {
        Optional<Meeting> meeting = meetingRepository.findById(id);
        String content = meeting.get().getContentOfMeeting();
        Long employeeId = employeeService.getEmployeeIdByLoggedInInformation();
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        String employeeNameAndSurname = employee.get().getName() + " " + employee.get().getSurname();
        String status = "REJECTED";
        Status stat = Status.REJECTED;
        List<Employee> employees = meeting.get().getEmployees();
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
        List<Meeting> meetings = meetingRepository.findAllMeetingsSortedByStartDateDescending();
        model.addAttribute("meetings", meetings);
        return "receptionists-allMeetings";
    }

    @PostMapping("/receptionists/allMeetings/search")
    public String searchMeetingsByDate(Model model, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                       LocalDate queryDate) {
        LocalDateTime startOfDay = queryDate.atStartOfDay();
        LocalDateTime endOfDay = queryDate.atTime(LocalTime.MAX);
        Sort sort = Sort.by(Sort.Direction.DESC, "startOfMeeting");
        List<Meeting> meetings = meetingRepository.findByStartOfMeetingBetween(startOfDay, endOfDay, sort);
        model.addAttribute("meetings", meetings);
        model.addAttribute("queryDate", queryDate);
        return "receptionists-allMeetings";
    }

    @GetMapping("/receptionists/allMeetings/pdf")
    public String generatePdfWithMeetings(Model model, @RequestParam(required = false)
                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate queryDate) {
        List<Meeting> meetings;
        if (queryDate != null) {
            LocalDateTime startOfDay = queryDate.atStartOfDay();
            LocalDateTime endOfDay = queryDate.atTime(LocalTime.MAX);
            Sort sort = Sort.by(Sort.Direction.DESC, "startOfMeeting");
            meetings = meetingRepository.findByStartOfMeetingBetween(startOfDay, endOfDay, sort);
        } else {
            Sort sort = Sort.by(Sort.Direction.DESC, "startOfMeeting");
            meetings = meetingRepository.findAll(sort);
        }
        model.addAttribute("meetings", meetings);
        model.addAttribute("queryDate", queryDate);
        return "receptionists-allMeetings";
    }
}
