package com.example.checkcheck.controller;

import com.example.checkcheck.dto.responseDto.StatusResponseDto;
import com.example.checkcheck.service.MemberService;
import com.example.checkcheck.service.mail.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MailController {

    private final MailService mailService;

    //수신동의
    @GetMapping("/email/agreement/{memberId}")
    public ResponseEntity<Object> emailApproved(@PathVariable Long memberId) {
        mailService.setEmailApproved(memberId);
        return new ResponseEntity<>(new StatusResponseDto("Success",""), HttpStatus.OK);
    }
    //수신거절
    @GetMapping("/email/opposite/{memberId}")
    public ResponseEntity<Object> emailOpposed(@PathVariable Long memberId) {
        mailService.setEmailOpposed(memberId);
        return new ResponseEntity<>(new StatusResponseDto("Success",""), HttpStatus.OK);
    }
}
