package com.example.checkcheck.service;

import com.example.checkcheck.dto.responseDto.RefreshTokenResponseDto;
import com.example.checkcheck.dto.responseDto.TokenFactory;
import com.example.checkcheck.model.RefreshToken;
import com.example.checkcheck.repository.RefreshTokenRepository;
import com.example.checkcheck.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.sasl.AuthenticationException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public TokenFactory accessAndRefreshTokenProcess(String username, HttpServletResponse response) {
        String refreshToken = jwtTokenProvider.createRefreshToken(username);
        String token = jwtTokenProvider.createToken(username);

        //        리프레시 토큰 HTTPonly
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        response.addCookie(cookie);
        response.setHeader("Authorization", "Bearer "+token);
        response.setHeader("Access-Token-Expire-Time", String.valueOf(30*60*1000L));

        return new TokenFactory(token, refreshToken);
    }

    @Transactional
    public RefreshTokenResponseDto refreshAccessToken(String refreshToken) throws AuthenticationException {
        try {
            String id = jwtTokenProvider.getPayload(refreshToken);
            RefreshToken refresh = refreshTokenRepository.findByTokenKey(id).orElse(null);
            String compareToken = refresh.getTokenValue();

            if (!compareToken.equals(refreshToken)) {
                throw new AuthenticationException("refresh token이 유효하지 않습니다.222");
            }

            if (!jwtTokenProvider.validateRefreshToken(refreshToken)) {
                throw new AuthenticationException("refresh token이 유효하지 않습니다.111");
            }

            String newAccessToken = jwtTokenProvider.createToken(id);

//            return new TokenFactory(newAccessToken);
            return new RefreshTokenResponseDto(newAccessToken);
        } catch (NullPointerException np) {
            throw new AuthenticationException("올바른 RefreshToken을 헤더에 넣어주세요");
        }
    }
}