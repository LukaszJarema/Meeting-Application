package com.jarema.lukasz.Meeting.Application.controllers;

import com.jarema.lukasz.Meeting.Application.dtos.VisitorDto;
import com.jarema.lukasz.Meeting.Application.models.Visitor;
import com.jarema.lukasz.Meeting.Application.services.VisitorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class VisitorController {
    public VisitorService visitorService;

    @Autowired
    public VisitorController(VisitorService visitorService) {
        this.visitorService = visitorService;
    }

    @GetMapping("/visitors/login")
    public String loginAsAVisitorPage() {
        return "visitors-login";
    }

    @GetMapping("/visitors/new")
    public String createVisitorForm(Model model) {
        Visitor visitor = new Visitor();
        model.addAttribute("visitor", visitor);
        return "visitors-create";
    }

    @PostMapping("/visitors/new")
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
}
