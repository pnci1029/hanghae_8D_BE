package com.example.checkcheck.service.social;

import com.example.checkcheck.dto.responseDto.social.SocialResponseDto;
import com.example.checkcheck.dto.responseDto.social.TokenFactory;
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

    //header ??? Content-type ??????
    //1???
    @Transactional
    public SocialResponseDto googleLogin(String code, HttpServletResponse response)
            throws JsonProcessingException {
        String getAccessToken = getAccessToken(code);
        // 1. "????????????" ??? "????????? ??????" ??????

        // 2. ???????????? ????????? API ??????
        GoogleUserInfoDto googleUserInfo = getGoogleUserInfo(getAccessToken);

        // 3. ?????????ID??? ???????????? ??????
        Member member = signupGoogleUser(googleUserInfo);

        //4. ?????? ????????? ??????
        forceLoginKakaoUser(member);

        // User ?????? ??????

        //  5. response Header??? JWT ?????? ??????
        TokenFactory tokenFactory1 = memberService.accessAndRefreshTokenProcess(member.getUserEmail(), response);
        RefreshToken refreshToken = new RefreshToken(member.getUserEmail(), tokenFactory1.getRefreshToken());

//        redisService.setValues(member.getUserEmail(), tokenFactory1.getRefreshToken());

//        ???????????????????????? & ???????????? ?????????
        Optional<RefreshToken> existToken = refreshTokenRepository.findByTokenKey(member.getUserEmail());
        if (existToken.isEmpty()) {
            refreshTokenRepository.save(refreshToken);
        }  else {
            existToken.get().setTokenKey(refreshToken.getTokenKey());
            existToken.get().setTokenValue(refreshToken.getTokenValue());
        }
//        ?????? ?????? ?????? ??????

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
//                (true, "????????? ??????",kakaoUser), HttpStatus.OK);
        return socialResponseDto;
    }

    //header ??? Content-type ??????
    //1???
    public String getAccessToken(String code) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //HTTP Body ??????
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", client_id);
        body.add("client_secret", clientSecret);
//        body.add("redirect_uri", "http://localhost:8080/user/signin/google");
//        body.add("redirect_uri", "http://localhost:3000/user/signin/google");
        body.add("redirect_uri", "https://www.chackcheck99.com/user/signin/google");
        body.add("code", code);


        //HTTP ?????? ?????????
        HttpEntity<MultiValueMap<String, String>> googleTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://oauth2.googleapis.com/token",
                HttpMethod.POST,
                googleTokenRequest,
                String.class
        );

        //HTTP ?????? (JSON) -> ????????? ?????? ??????
        //JSON -> JsonNode ????????? ??????
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        String accessToken = jsonNode.get("access_token").asText();
//        String refreshToken = jsonNode.get("refresh_token").asText();
        String refreshToken = null;
//        return new TokenFactory(accessToken, refreshToken);
        return accessToken;
    }

    //2???
    public GoogleUserInfoDto getGoogleUserInfo(String accessToken) throws JsonProcessingException {
        // HTTP Header ??????
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        // HTTP ?????? ?????????
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://openidconnect.googleapis.com/v1/userinfo",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );
        //HTTP ?????? (JSON)
        //JSON -> JsonNode ????????? ??????
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        String id = jsonNode.get("sub").asText();

        String userEmail = jsonNode.get("email").asText();

        String userName = jsonNode.get("name").asText();
        return new GoogleUserInfoDto(id, userName, userEmail);
    }

    // 3???
    private Member signupGoogleUser(GoogleUserInfoDto googleUserInfoDto) {
        // ????????? ??????
        // DB ??? ????????? Kakao Id ??? ????????? ??????
        Member findGoogle = memberRepository.findByUserRealEmail(googleUserInfoDto.getUserEmail()).orElse(null);


        //DB??? ????????? ????????? ????????? ???????????? ??????
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

    // 4???
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
