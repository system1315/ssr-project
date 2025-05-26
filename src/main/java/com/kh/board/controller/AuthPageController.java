package com.kh.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthPageController {
    @GetMapping("/auth/login")
    public String loginForm() {
        return "auth/login";
    }
    @GetMapping("/auth/register")
    public String registerForm() {
        return "auth/register";
    }
} 