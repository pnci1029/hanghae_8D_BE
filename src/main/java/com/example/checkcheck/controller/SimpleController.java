package com.example.checkcheck.controller;

import com.example.checkcheck.security.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class SimpleController {

    @GetMapping("/")
    public String simpleCon() {
        log.info("인포");
        log.warn("경고");
        log.debug("디버그");

        return "수정33";

    }
    @GetMapping("/health")
    public String checkHealth() {
        return "healthy";
    }

}
