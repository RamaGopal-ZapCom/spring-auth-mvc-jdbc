package com.example.auth.controller;

import com.example.auth.model.User;
import com.example.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.*;

@Controller
public class HomeController {

    @Autowired
    private AuthService authService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String showLogin() {
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpServletRequest request) {
        User user = authService.authenticate(username, password);
        if (user != null) {
            request.getSession().setAttribute("user", user);
            return "redirect:/home";
        }
        return "login";
    }

    @RequestMapping("/home")
    public String home() {
        return "home";
    }

    @RequestMapping("/admin")
    public String admin(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (!"ADMIN".equals(user.getRole())) {
            return "unauthorized";
        }
        return "admin";
    }



    @RequestMapping("/user")
    public String userPage(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        // Allow both USER and ADMIN roles here if needed
        if (!"USER".equals(user.getRole()) && !"ADMIN".equals(user.getRole())) {
            return "unauthorized";
        }
        return "user";  // create user.jsp for this
    }

    @RequestMapping("/unauthorized")
    public String unauthorized() {
        return "unauthorized";
    }

}