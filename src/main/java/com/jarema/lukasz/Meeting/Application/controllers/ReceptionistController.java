package com.jarema.lukasz.Meeting.Application.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReceptionistController {

    @GetMapping("/receptionists/login")
    public String loginAsAReceptionistPage() {
        return "receptionists-login";
    }
}
