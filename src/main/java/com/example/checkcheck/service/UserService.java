package com.example.checkcheck.service;

import com.example.checkcheck.dto.requestDto.RefreshTokenRequestDto;
import com.example.checkcheck.dto.responseDto.TokenFactory;
import com.example.checkcheck.repository.RefreshTokenRepository;
import com.example.checkcheck.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public String accessAndRefreshTokenProcess(String username, HttpServletResponse response) {
        String refreshToken = jwtTokenProvider.createRefreshToken();
        String token = jwtTokenProvider.createToken(username);
        response.setHeader("Authorization", "Bearer "+token);
        response.setHeader("Access-Token-Expire-Time", String.valueOf(30*60*1000L));
        System.out.println("token111 = " + token);
        return token;
    }

    @Transactional
    public TokenFactory refreshAccessToken(String accessToken, RefreshTokenRequestDto refreshTokenRequest) throws AuthenticationException {
        String refreshToken = refreshTokenRequest.getRefreshToken();
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new AuthenticationException("refresh token이 유효하지 않습니다.");
        }

        String id = jwtTokenProvider.getPayload(accessToken);
//        String existingRefreshToken = refreshTokenRepository.findByTokenValue(id);
        String existingRefreshToken = String.valueOf(refreshTokenRepository.findByTokenValue(id));

        if (!existingRefreshToken.equals(refreshToken)) {
            throw new AuthenticationException("refresh token이 유효하지 않습니다.");
        }

        String newAccessToken = jwtTokenProvider.createToken(id);


        return new TokenFactory(newAccessToken);
    }
}