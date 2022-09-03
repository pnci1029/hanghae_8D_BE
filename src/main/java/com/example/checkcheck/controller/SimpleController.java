package com.example.checkcheck.controller;

import com.example.checkcheck.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleController {

    @GetMapping("/lalalalala")
    public String simpleCon(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("name = " + name);
        System.out.println("userDetails username= " + userDetails.getUsername());
        return userDetails.getUsername();

    }
}
