package com.jarema.lukasz.Meeting.Application.controllers;

import com.jarema.lukasz.Meeting.Application.dtos.EmployeeDto;
import com.jarema.lukasz.Meeting.Application.dtos.VisitorDto;
import com.jarema.lukasz.Meeting.Application.models.Meeting;
import com.jarema.lukasz.Meeting.Application.models.Visitor;
import com.jarema.lukasz.Meeting.Application.repositories.EmployeeRepository;
import com.jarema.lukasz.Meeting.Application.services.EmployeeService;
import com.jarema.lukasz.Meeting.Application.services.VisitorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
    public VisitorController(VisitorService visitorService, EmployeeRepository employeeRepository, EmployeeService
                             employeeService) {
        this.visitorService = visitorService;
        this.employeeRepository = employeeRepository;
        this.employeeService = employeeService;
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

    @GetMapping("/visitors/createAMeeting")
    public String createMeetingForm(Model model) {
        model.addAttribute("meeting", new Meeting());
        List<EmployeeDto> employees = employeeService.findAllEmployees();
        model.addAttribute("employees", employees);
        return "visitors-createAMeeting";
    }

    /*
    @PostMapping("/visitors/createAMeeting")
    public String saveMeeting(@Valid @ModelAttribute("meeting") MeetingDto meetingDto, BindingResult result, Model model) {
        List<String> emailAddresses = new ArrayList<>();
        emailAddressess.add

    }

     */
}
