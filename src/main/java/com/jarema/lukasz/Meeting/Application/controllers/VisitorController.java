package com.jarema.lukasz.Meeting.Application.controllers;

import com.jarema.lukasz.Meeting.Application.models.Visitor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VisitorController {

    @GetMapping("/visitors/new")
    public String createVisitorForm(Model model) {
        Visitor visitor = new Visitor();
        model.addAttribute("visitor", visitor);
        return "visitors-create";
    }
}
