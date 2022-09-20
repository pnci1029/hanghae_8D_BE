package com.example.checkcheck.security;

import com.example.checkcheck.dto.responseDto.ResponseDto;
import com.example.checkcheck.exception.CustomException;
import com.example.checkcheck.exception.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 헤더에서 jwt 토큰 받아옴
        try {
            String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);

            // 유효한 토큰인지 확인
            if (token != null && jwtTokenProvider.validateToken(token)) {
                // 토큰이 유효하면 토큰으로부터 유저 정보를 받아와서 저장
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else if (token != null && !jwtTokenProvider.validateToken(token)) {
                String result = jwtTokenProvider.resolveRefreshToken((HttpServletRequest) request);
                if (result == null) {
                    throw new JwtException("액세스 토큰이 존재하지 않습니다.");
                }
            }
        } catch (ExpiredJwtException e) {
            request.setAttribute("exception", ErrorCode.EXPIRED_TOKEN.getErrorCode());
        } catch (JwtException e) {
            request.setAttribute("exception", ErrorCode.UnSupported_Token);
        } catch (NullPointerException e) {
            request.setAttribute("exception", ErrorCode.NullPoint_Token);
        }

        chain.doFilter(request, response);


    }



}

