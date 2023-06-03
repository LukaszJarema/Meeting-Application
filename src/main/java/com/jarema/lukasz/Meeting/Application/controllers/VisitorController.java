package com.jarema.lukasz.Meeting.Application.controllers;

import com.jarema.lukasz.Meeting.Application.dtos.MeetingDto;
import com.jarema.lukasz.Meeting.Application.dtos.VisitorDto;
import com.jarema.lukasz.Meeting.Application.models.Employee;
import com.jarema.lukasz.Meeting.Application.models.Meeting;
import com.jarema.lukasz.Meeting.Application.models.Visitor;
import com.jarema.lukasz.Meeting.Application.repositories.EmployeeRepository;
import com.jarema.lukasz.Meeting.Application.repositories.VisitorRepository;
import com.jarema.lukasz.Meeting.Application.services.EmployeeService;
import com.jarema.lukasz.Meeting.Application.services.MeetingService;
import com.jarema.lukasz.Meeting.Application.services.VisitorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class VisitorController {
    @Autowired
    public VisitorService visitorService;
    @Autowired
    public EmployeeRepository employeeRepository;
    @Autowired
    public EmployeeService employeeService;
    @Autowired
    public MeetingService meetingService;
    @Autowired
    public VisitorRepository visitorRepository;

    @Autowired
    public VisitorController(VisitorService visitorService, EmployeeRepository employeeRepository, EmployeeService
                             employeeService, MeetingService meetingService, VisitorRepository visitorRepository) {
        this.visitorService = visitorService;
        this.employeeRepository = employeeRepository;
        this.employeeService = employeeService;
        this.meetingService = meetingService;
        this.visitorRepository = visitorRepository;
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
        String nameOfVisitor;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            nameOfVisitor = ((UserDetails)principal).getUsername();
        } else {
            nameOfVisitor = principal.toString();
        }
        Long visitorId = visitorRepository.findByEmailAddress(nameOfVisitor).getId();
        meetingDto.setEmployeeIds(employeeIds);
        meetingDto.setVisitor(visitorRepository.findById(visitorId).orElse(null));
        meetingService.createMeeting(visitorId, meetingDto);
        return "redirect:/visitors/home";
    }
}
