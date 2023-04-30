package com.jarema.lukasz.Meeting.Application.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/loginAsAnAdmin")
    public String loginAsAnAdministratorPage() {
        return "administrators-login";
    }

    @GetMapping("/loginAsAnEmployee")
    public String loginAsAnEmployeePage() {
        return "employees-login";
    }

    /*
    @PostMapping("/loginAsAnEmployee")
    public String loginAsAnEmployee(@ModelAttribute("employee")Employee employee) {
        return "/employees-list";
    }

     */

    @GetMapping("/loginAsAReceptionist")
    public String loginAsAReceptionistPage() {
        return "receptionists-login";
    }

    @GetMapping("/loginAsAVisitor")
    public String loginAsAVisitorPage() {
        return "visitors-login";
    }
}
