package com.example.checkcheck.security;


import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;
    public static final String AUTH_HEADER = "Authorization";
    public final HttpServletResponse response;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    // 토큰 유효시간
    // 프론트엔드와 약속해야 함
    private final Long tokenValidTime = 30*60*1000L;  // 30분
//    private final Long tokenValidTime = 20*1000L;  // 20초
    private final Long refreshTokenValidTime = 7*24*60*60*1000L;  // 1주일

    private final UserDetailsService userDetailsService;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // 토큰 생성
    public String createToken(String userPk) {
        Claims claims = Jwts.claims().setSubject(userPk);
        Date now = new Date();
        String token= Jwts.builder()
                .setClaims(claims)//정보저장
                .setIssuedAt(now)//토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)//사용할 암호화 알고리즘
                //signature에 들어갈 secret값 세팅
                .compact();

        response.addHeader(AUTH_HEADER,"Bearer " + token);
//        헤더에넣기
        return token;
    }

    public String createRefreshToken(String username) {
        Date now = new Date();
        String refreshToken= Jwts.builder()
                .setIssuedAt(now)
                .setSubject(username)
                .setExpiration(new Date(now.getTime() + refreshTokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        response.addHeader("RefreshToken","Bearer " + refreshToken);
        return refreshToken;
    }

    // 토큰에서 회원 정보 추출
    public String getUserPk(String jwtToken) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken).getBody().getSubject();
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String jwtToken) {


        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(this.getUserPk(setTokenName(jwtToken)));
        return new UsernamePasswordAuthenticationToken(userDetails, jwtToken, userDetails.getAuthorities());
    }

    //Request의 Header에서 token 값을 가져옴
    //"X-AUTH-TOKEN":"TOKEN 깞"
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }
    public String cutToken(String token){
        if (StringUtils.hasText(token) && token.startsWith("Bearer")) {
            return token.substring(7);
        }
        return null;
    }
    public String resolveRefreshToken(HttpServletRequest request) {
        return request.getHeader("RefreshToken");
    }


    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(setTokenName(token));

            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public boolean validateRefreshToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(setTokenName(token));

            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // Bearer 삭제
    private String setTokenName(String bearerToken){
        return bearerToken.replace("Bearer ", "");
    }


    public String getPayload(String token) throws AuthenticationException {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject();
        } catch (JwtException e) {
            throw new AuthenticationException("유효하지 않은 토큰입니다.");
        }
    }



}

