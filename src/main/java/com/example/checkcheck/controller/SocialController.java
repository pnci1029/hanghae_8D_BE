package com.example.checkcheck.controller;

import com.example.checkcheck.dto.responseDto.RefreshTokenResponseDto;
import com.example.checkcheck.dto.responseDto.SocialResponseDto;
import com.example.checkcheck.dto.responseDto.TokenFactory;
import com.example.checkcheck.security.JwtTokenProvider;
import com.example.checkcheck.service.MemberService;
import com.example.checkcheck.service.social.SocialGoogleService;
import com.example.checkcheck.service.social.SocialKakaoService;
import com.example.checkcheck.service.social.SocialNaverSerivce;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SocialController {

    private final SocialKakaoService socialKakaoService;
    private final SocialNaverSerivce socialNaverSerivce;
    private final SocialGoogleService socialGoogleService;
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    //소셜 카카오 로그인
    @GetMapping("/user/signin/kakao")
    public SocialResponseDto kakaoLogin(
            @RequestParam(value = "code") String code, HttpServletResponse response) throws JsonProcessingException {
        return socialKakaoService.kakaoLogin(code, response);
    }

    @GetMapping("/user/signin/naver")
    public SocialResponseDto naverLogin(
            @RequestParam(value = "code") String code, String state, HttpServletResponse response) {
        return socialNaverSerivce.naverLogin(code, state, response);
    }

    @GetMapping("/user/signin/google")
    public SocialResponseDto googleLogin(
            @RequestParam(value = "code") String code, HttpServletResponse response) throws JsonProcessingException {
        return socialGoogleService.googleLogin(code, response);
    }

    @GetMapping(value = "/auth/user/token")
    public RefreshTokenResponseDto refreshAccessToken(HttpServletRequest request
    ) throws AuthenticationException {
        String refreshToken = jwtTokenProvider.resolveRefreshToken(request);
        return memberService.refreshAccessToken(refreshToken);
    }


}
