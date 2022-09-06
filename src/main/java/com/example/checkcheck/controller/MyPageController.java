package com.example.checkcheck.controller;

import com.example.checkcheck.dto.responseDto.ResponseDto;
import com.example.checkcheck.security.UserDetailsImpl;
import com.example.checkcheck.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping("/api/auth/profile")
    public ResponseDto<?> readMyPage(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return myPageService.readMyPage(userDetails);
    }

    @GetMapping("/api/auth/profile/list/{articleId}")
    public ResponseDto<?> readMyPageArticle(@PathVariable Long articleId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return myPageService.readMyPageArticle(articleId, userDetails);
    }


    @DeleteMapping("/api/auth/profile/")
    public ResponseDto<?> deleteMember(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return myPageService.deleteMember(userDetails);
    }
}
