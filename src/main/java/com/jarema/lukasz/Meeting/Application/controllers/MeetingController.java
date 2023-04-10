package com.jarema.lukasz.Meeting.Application.controllers;

import com.jarema.lukasz.Meeting.Application.dtos.EmployeeDto;
import com.jarema.lukasz.Meeting.Application.repositories.EmployeeRepository;
import com.jarema.lukasz.Meeting.Application.services.EmployeeService;
import com.jarema.lukasz.Meeting.Application.services.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MeetingController {
    @Autowired
    private MeetingService meetingService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @GetMapping("/meeting")
    public String createMeeting(Model model) {
        List<EmployeeDto> employees = employeeService.findAllEmployees();
        model.addAttribute("employees", employees);
        return "meetings-create";
    }
}
