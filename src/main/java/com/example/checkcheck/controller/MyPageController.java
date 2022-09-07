package com.example.checkcheck.controller;

import com.example.checkcheck.dto.responseDto.ResponseDto;
import com.example.checkcheck.model.articleModel.Process;
import com.example.checkcheck.repository.ArticleRepository;
import com.example.checkcheck.security.UserDetailsImpl;
import com.example.checkcheck.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    public ResponseDto<?> readMyPageArticle(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @RequestParam(value = "process")Process process) {

        return(userDetails != null) ? ResponseDto.success(articleRepository.myPageInfo(userDetails, process))
                                        :ResponseDto.fail("400","aaaaaaaaa");
//        return myPageService.readMyPageArticle(userDetails, process);
    }


    @DeleteMapping("/api/auth/profile")
    public ResponseDto<?> deleteMember(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return myPageService.deleteMember(userDetails);
    }
}
