package com.jarema.lukasz.Meeting.Application.controllers;

import com.jarema.lukasz.Meeting.Application.dtos.MeetingDto;
import com.jarema.lukasz.Meeting.Application.dtos.VisitorDto;
import com.jarema.lukasz.Meeting.Application.models.Employee;
import com.jarema.lukasz.Meeting.Application.models.Meeting;
import com.jarema.lukasz.Meeting.Application.models.Visitor;
import com.jarema.lukasz.Meeting.Application.repositories.EmployeeRepository;
import com.jarema.lukasz.Meeting.Application.repositories.MeetingRepository;
import com.jarema.lukasz.Meeting.Application.repositories.VisitorRepository;
import com.jarema.lukasz.Meeting.Application.services.EmailService;
import com.jarema.lukasz.Meeting.Application.services.EmployeeService;
import com.jarema.lukasz.Meeting.Application.services.MeetingService;
import com.jarema.lukasz.Meeting.Application.services.VisitorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
public class VisitorController {

    private VisitorService visitorService;
    private EmployeeRepository employeeRepository;
    private EmployeeService employeeService;
    private MeetingService meetingService;
    private VisitorRepository visitorRepository;
    private PasswordEncoder passwordEncoder;
    private MeetingRepository meetingRepository;
    private EmailService emailService;

    @Autowired
    public VisitorController(VisitorService visitorService, EmployeeRepository employeeRepository, EmployeeService
                             employeeService, MeetingService meetingService, VisitorRepository visitorRepository,
                             PasswordEncoder passwordEncoder, MeetingRepository meetingRepository,
                             EmailService emailService) {
        this.visitorService = visitorService;
        this.employeeRepository = employeeRepository;
        this.employeeService = employeeService;
        this.meetingService = meetingService;
        this.visitorRepository = visitorRepository;
        this.passwordEncoder = passwordEncoder;
        this.meetingRepository = meetingRepository;
        this.emailService = emailService;
    }

    @GetMapping("/register")
    public String createVisitorForm(Model model) {
        Visitor visitor = new Visitor();
        model.addAttribute("visitor", visitor);
        return "visitors-create";
    }

    @PostMapping("/register")
    public String saveVisitor(@Valid @ModelAttribute("visitor") VisitorDto visitorDto, BindingResult result,
                              Model model) {
        Visitor exsistingVisitorEmailAddress = visitorService.findByEmail(visitorDto.getEmailAddress());
        if(exsistingVisitorEmailAddress != null && exsistingVisitorEmailAddress.getEmailAddress() != null && !exsistingVisitorEmailAddress.getEmailAddress().isEmpty()) {
            result.rejectValue("emailAddress", "error.emailAddress", "There is already a Visitor with this email address or username");
        }
        if(result.hasErrors()) {
            model.addAttribute("visitor", visitorDto);
            return "visitors-create";
        }
        visitorService.saveVisitor(visitorDto);
        return "redirect:/meeting?success";
    }

    @GetMapping("/visitors/home")
    public String viewVisitorHomePage() {
        return "visitors-home";
    }

    @GetMapping("/visitors/new-meeting")
    public String createMeetingForm(Model model) {
        List<Employee> employeeList = employeeRepository.findAll();
        model.addAttribute("employeeList", employeeList);
        model.addAttribute("meeting", new Meeting());
        return "visitors-createAMeeting";
    }

    @PostMapping("/visitors/new-meeting")
    public String saveMeeting(@Valid @ModelAttribute("meeting") MeetingDto meetingDto,
                              @RequestParam("employeeIds") List<Long> employeeIds, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("meeting", meetingDto);
            return "visitors-createAMeeting";
        }
        Long visitorId = visitorService.getVisitorIdByLoggedInInformation();
        meetingDto.setEmployeeIds(employeeIds);
        meetingDto.setVisitor(visitorRepository.findById(visitorId).orElse(null));
        meetingService.createMeeting(visitorId, meetingDto);
        return "redirect:/visitors/home";
    }

    @GetMapping("/visitors/edit")
    public String visitorEditPage(Model model) {
        Long visitorId = visitorService.getVisitorIdByLoggedInInformation();
        Optional<Visitor> visitor = visitorRepository.findById(visitorId);
        model.addAttribute("visitor", visitor);
        return "visitors-edit";
    }

    @PostMapping("/visitors/edit")
    public String updateVisitor(@Valid @ModelAttribute("visitor") VisitorDto visitor, BindingResult result,
                                Model model) {
        if (result.hasErrors()) {
            model.addAttribute("visitor", visitor);
            return "visitors-edit";
        }
        Long visitorId = visitorService.getVisitorIdByLoggedInInformation();
        visitor.setId(visitorId);
        visitorService.updateVisitor(visitor);
        return "redirect:/visitors/home";
    }

    @GetMapping("/visitors/changePassword")
    public String visitorChangePasswordPage(Model model) {
        Long visitorId = visitorService.getVisitorIdByLoggedInInformation();
        Optional<Visitor> visitor = visitorRepository.findById(visitorId);
        model.addAttribute("visitor", visitor);
        return "visitors-changePassword";
    }

    @PostMapping("/visitors/changePassword")
    public String updateVisitorPassword(@Valid @RequestParam(value = "password") String password,
                                        @ModelAttribute("visitor") VisitorDto visitor, BindingResult result,
                                        Model model) {
        if (result.hasErrors() || password.length() < 6) {
            model.addAttribute(("visitor"), visitor);
            return "visitors-changePassword";
        }
        Long visitorId = visitorService.getVisitorIdByLoggedInInformation();
        visitor.setId(visitorId);
        String encodePassword = passwordEncoder.encode(password);
        visitorRepository.updateVisitorPassword(encodePassword, visitorId);
        return "redirect:/visitors/home";
    }

    @GetMapping("/visitors/myMeetings")
    public String visitorMyMeetingsPage(Model model, Principal principal) {
        String visitorEmailAddress = principal.getName();
        Visitor visitor = visitorRepository.findByEmailAddress(visitorEmailAddress);
        model.addAttribute("visitor", visitor);
        List<Meeting> meetings;
        meetings = visitor.getMeetings();
        meetings.sort(Comparator.comparing(Meeting::getStartOfMeeting).reversed());
        model.addAttribute("meetings", meetings);
        return "visitors-myMeetings";
    }

    @PostMapping("/visitors/myMeetings/search")
    public String searchVisitorMeetings(Model model, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                        LocalDate queryDate, Principal principal) {
        String visitorEmailAddress = principal.getName();
        Visitor visitor = visitorRepository.findByEmailAddress(visitorEmailAddress);
        model.addAttribute("visitor", visitor);
        LocalDateTime startOfDay = queryDate.atStartOfDay();
        LocalDateTime endOfDay = queryDate.atTime(LocalTime.MAX);
        Sort sort = Sort.by(Sort.Direction.DESC, "startOfMeeting");
        List<Meeting> meetings = meetingRepository.findByStartOfMeetingBetweenAndVisitor(startOfDay, endOfDay,
                visitor, sort);
        model.addAttribute("meetings", meetings);
        model.addAttribute("queryDate", queryDate);
        return "visitors-myMeetings";
    }

    @PostMapping("visitors/myMeetings/{id}/delete")
    @Transactional
    public String deleteMeeting(@PathVariable Long id, Principal principal) {
        String visitorEmailAddress = principal.getName();
        Optional<Meeting> meeting = meetingRepository.findById(id);
        String visitor = meeting.get().getVisitor().getName() + " " + meeting.get().getVisitor().getSurname();
        String contentOfMeeting = meeting.get().getContentOfMeeting();
        LocalDateTime date = meeting.get().getStartOfMeeting();
        List<Employee> employees = meeting.get().getEmployees();
        for (Employee employee : employees) {
            emailService.sendInformationAboutDeletedMeetingToEmployee(employee.getEmailAddress(), visitor,
                    contentOfMeeting, date);
        }
        emailService.sendConfirmationAboutDeletedMeetingToVisitor(visitorEmailAddress);
        for (Employee employee : meeting.get().getEmployees()) {
            employee.getMeeting().remove(meeting.get());
        }
        meetingService.delete(id);
        return "redirect:/visitors/home";
    }
}
