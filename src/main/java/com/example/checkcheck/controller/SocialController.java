package com.example.checkcheck.controller;

import com.example.checkcheck.dto.responseDto.SocialResponseDto;
import com.example.checkcheck.dto.responseDto.TokenFactory;
import com.example.checkcheck.security.JwtTokenProvider;
import com.example.checkcheck.service.MemberService;
import com.example.checkcheck.service.social.SocialGoogleService;
import com.example.checkcheck.service.social.SocialKakaoService;
import com.example.checkcheck.service.social.SocialNaverSerivce;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class SocialController {

    private final SocialKakaoService socialKakaoService;
    private final SocialNaverSerivce socialNaverSerivce;
    private final SocialGoogleService socialGoogleService;
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    //소셜 카카오 로그인
    @GetMapping("/signin/kakao")
    public SocialResponseDto kakaoLogin(
            @RequestParam(value = "code") String code, HttpServletResponse response) throws JsonProcessingException {
        return socialKakaoService.kakaoLogin(code, response);
    }

    @GetMapping("/signin/naver")
    public SocialResponseDto naverLogin(
            @RequestParam(value = "code") String code, String state, HttpServletResponse response) {
        return socialNaverSerivce.naverLogin(code, state, response);
    }

    @GetMapping("/signin/google")
    public SocialResponseDto googleLogin(
            @RequestParam(value = "code") String code, HttpServletResponse response) throws JsonProcessingException {
        return socialGoogleService.googleLogin(code, response);
    }

    @GetMapping(value = "/token")
    public TokenFactory refreshAccessToken(HttpServletRequest request
//                                           @ModelAttribute (value = "refreshToken")RefreshTokenRequestDto refreshToken
    ) throws AuthenticationException {
        String refreshToken = jwtTokenProvider.resolveRefreshToken(request);
        return memberService.refreshAccessToken(refreshToken);
    }


}
