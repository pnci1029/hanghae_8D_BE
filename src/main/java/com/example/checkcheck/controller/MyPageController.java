package com.example.checkcheck.controller;

import com.example.checkcheck.dto.responseDto.MyPageResponseDto;
import com.example.checkcheck.dto.responseDto.ResponseDto;
import com.example.checkcheck.model.articleModel.Process;
import com.example.checkcheck.repository.ArticleRepository;
import com.example.checkcheck.security.UserDetailsImpl;
import com.example.checkcheck.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MyPageController {

    private final MyPageService myPageService;
    private final ArticleRepository articleRepository;

    @GetMapping("/api/auth/profile")
    public ResponseDto<?> readMyPage(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return myPageService.readMyPage(userDetails);
    }

    @GetMapping("/api/auth/profile/list")
    public List<MyPageResponseDto> readMyPageArticle(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                     @RequestParam(value = "process") Process process) {

        return articleRepository.myPageInfo(userDetails, process);
    }


    @DeleteMapping("/api/auth/profile")
    public ResponseDto<?> deleteMember(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return myPageService.deleteMember(userDetails);
    }

    @PatchMapping("/api/auth/profile/change")
    public ResponseDto<?> changeNickName(@RequestParam(value = "nickname") String nickName,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return myPageService.changeNickName(nickName, userDetails);
    }
}
