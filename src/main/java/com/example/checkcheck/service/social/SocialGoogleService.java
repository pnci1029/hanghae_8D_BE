package com.example.checkcheck.service.social;

import com.example.checkcheck.dto.responseDto.SocialResponseDto;
import com.example.checkcheck.dto.responseDto.TokenFactory;
import com.example.checkcheck.dto.userinfo.GoogleUserInfoDto;
import com.example.checkcheck.exception.CustomException;
import com.example.checkcheck.exception.ErrorCode;
import com.example.checkcheck.model.Member;
import com.example.checkcheck.model.RefreshToken;
import com.example.checkcheck.repository.MemberRepository;
import com.example.checkcheck.repository.RefreshTokenRepository;
import com.example.checkcheck.security.UserDetailsImpl;
import com.example.checkcheck.service.MemberService;
import com.example.checkcheck.service.RedisService;
import com.example.checkcheck.util.ComfortUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class SocialGoogleService {

    @Value("${cloud.security.oauth2.client.registration.google.client-id}")
    String client_id;
    @Value("${cloud.security.oauth2.client.registration.google.client-secret}")
    String clientSecret;

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ComfortUtils comfortUtils;

//    private final RedisService redisService;

    //header 에 Content-type 지정
    //1번
    @Transactional
    public SocialResponseDto googleLogin(String code, HttpServletResponse response)
            throws JsonProcessingException {
        String getAccessToken = getAccessToken(code);
        // 1. "인가코드" 로 "액세스 토큰" 요청

        // 2. 토큰으로 카카오 API 호출
        GoogleUserInfoDto googleUserInfo = getGoogleUserInfo(getAccessToken);

        // 3. 카카오ID로 회원가입 처리
        Member member = signupGoogleUser(googleUserInfo);

        //4. 강제 로그인 처리
        forceLoginKakaoUser(member);

        // User 권한 확인

        //  5. response Header에 JWT 토큰 추가
        TokenFactory tokenFactory1 = memberService.accessAndRefreshTokenProcess(member.getUserEmail(), response);
        RefreshToken refreshToken = new RefreshToken(member.getUserEmail(), tokenFactory1.getRefreshToken());

//        redisService.setValues(member.getUserEmail(), tokenFactory1.getRefreshToken());

//        리프레시토큰저장 & 있을경우 셋토큰
        Optional<RefreshToken> existToken = refreshTokenRepository.findByTokenKey(member.getUserEmail());
        if (existToken.isEmpty()) {
            refreshTokenRepository.save(refreshToken);
        }  else {
            existToken.get().setTokenKey(refreshToken.getTokenKey());
            existToken.get().setTokenValue(refreshToken.getTokenValue());
        }
//        유저 알림 유무 조회

        boolean alarmStatus = comfortUtils.getAlarmStatus(member.getNotification());

        SocialResponseDto socialResponseDto = SocialResponseDto.builder()
                .userEmail(member.getUserEmail())
                .nickName(member.getNickName())
                .accessToken(tokenFactory1.getAccessToken())
                .userRank(comfortUtils.getUserRank(member.getPoint()))
                .refreshToken(tokenFactory1.getRefreshToken())
//                .jwtToken("Bearer "+jwtToken)
                .isAccepted(member.getIsAccepted())
                .alarmStatus(alarmStatus)
                .build();

//        return new ResponseEntity<>(new FinalResponseDto<>
//                (true, "로그인 성공",kakaoUser), HttpStatus.OK);
        return socialResponseDto;
    }

    //header 에 Content-type 지정
    //1번
    public String getAccessToken(String code) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", client_id);
        body.add("client_secret", clientSecret);
//        body.add("redirect_uri", "http://localhost:8080/user/signin/google");
//        body.add("redirect_uri", "http://localhost:3000/user/signin/google");
        body.add("redirect_uri", "https://www.chackcheck99.com/user/signin/google");
        body.add("code", code);


        //HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> googleTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://oauth2.googleapis.com/token",
                HttpMethod.POST,
                googleTokenRequest,
                String.class
        );

        //HTTP 응답 (JSON) -> 액세스 토큰 파싱
        //JSON -> JsonNode 객체로 변환
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        String accessToken = jsonNode.get("access_token").asText();
//        String refreshToken = jsonNode.get("refresh_token").asText();
        String refreshToken = null;
//        return new TokenFactory(accessToken, refreshToken);
        return accessToken;
    }

    //2번
    public GoogleUserInfoDto getGoogleUserInfo(String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://openidconnect.googleapis.com/v1/userinfo",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );
        //HTTP 응답 (JSON)
        //JSON -> JsonNode 객체로 변환
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        String id = jsonNode.get("sub").asText();

        String userEmail = jsonNode.get("email").asText();

        String userName = jsonNode.get("name").asText();
        return new GoogleUserInfoDto(id, userName, userEmail);
    }

    // 3번
    private Member signupGoogleUser(GoogleUserInfoDto googleUserInfoDto) {
        // 재가입 방지
        // DB 에 중복된 Kakao Id 가 있는지 확인
        Member findGoogle = memberRepository.findByUserRealEmail(googleUserInfoDto.getUserEmail()).orElse(null);


        //DB에 중복된 계정이 없으면 회원가입 처리
        if (findGoogle == null) {
            String userName = googleUserInfoDto.getNickname();
            String email = googleUserInfoDto.getUserEmail();
            String password = UUID.randomUUID().toString();
            String encodedPassword = password;
            String provider = "google";

            Member kakaoMember = Member.builder()
                    .userName(userName)
                    .nickName(comfortUtils.makeUserNickName())
                    .userEmail("g_" + email)
                    .userRealEmail(email)
                    .password(encodedPassword)
                    .provider(provider)
                    .isAccepted(false)
                    .isDeleted(false)
                    .build();
            memberRepository.save(kakaoMember);


            return kakaoMember;
        }

        return findGoogle;
    }

    // 4번
    public Authentication forceLoginKakaoUser(Member googleMember) {
        UserDetails userDetails = new UserDetailsImpl(googleMember);
        if (googleMember.getIsDeleted().equals(true)) {
            throw new CustomException(ErrorCode.DELETED_USER_EXCEPTION);
        }
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }
}
