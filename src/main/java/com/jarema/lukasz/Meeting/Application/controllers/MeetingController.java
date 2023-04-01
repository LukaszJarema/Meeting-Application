package com.jarema.lukasz.Meeting.Application.controllers;

import com.jarema.lukasz.Meeting.Application.dtos.EmployeeDto;
import com.jarema.lukasz.Meeting.Application.models.Employee;
import com.jarema.lukasz.Meeting.Application.models.Meeting;
import com.jarema.lukasz.Meeting.Application.services.EmployeeService;
import com.jarema.lukasz.Meeting.Application.services.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class MeetingController {
    private MeetingService meetingService;
    private EmployeeService employeeService;

    @Autowired
    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @GetMapping("/meeting")
    public String createMeeting(Model model) {
        List<EmployeeDto> employeeList = employeeService.findAllEmployees();
        model.addAttribute("meeting", new Meeting());
        model.addAttribute("employeeList", employeeList);

        for (int i = 0; i < employeeList.size(); i++)
            System.out.printf(String.valueOf(employeeList.get(i)));

        return "meetings-create";
    }
}
