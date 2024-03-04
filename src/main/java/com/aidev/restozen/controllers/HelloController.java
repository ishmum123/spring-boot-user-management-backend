package com.aidev.restozen.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("hello")
public class HelloController {

    @GetMapping("customer")
    public String helloUser() {
        return "Hello User";
    }

    @GetMapping("admin")
    public String hello(Authentication authentication) {
        return "Hello, Admin:" + authentication.getName() + "!";
    }

}