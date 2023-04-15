package com.jarema.lukasz.Meeting.Application.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String mainPage(){
        return "main-page";
    }

    @GetMapping("/loginAsAVisitor")
    public String loginAsAVisitorPage() {
        return "visitors-login";
    }
}
