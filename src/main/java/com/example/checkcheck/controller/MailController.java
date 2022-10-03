package com.example.checkcheck.controller;

import com.example.checkcheck.dto.requestDto.MailStatusRequestDto;
import com.example.checkcheck.dto.responseDto.ResponseDto;
import com.example.checkcheck.dto.responseDto.StatusResponseDto;
import com.example.checkcheck.security.UserDetailsImpl;
import com.example.checkcheck.service.MemberService;
import com.example.checkcheck.service.mail.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MailController {

    private final MailService mailService;

    @PatchMapping("/auth/profile/email")
    public ResponseDto<?> emailApproved(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                     @RequestBody MailStatusRequestDto mailStatusRequestDto) {

        return mailService.setEmailAgreementStatus(mailStatusRequestDto, userDetails);
    }

}
