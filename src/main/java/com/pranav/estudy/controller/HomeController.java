package com.pranav.estudy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// Redirects root URL to login page
@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "redirect:/login.html";
    }
}
