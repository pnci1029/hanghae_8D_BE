package com.example.checkcheck.service.social;

import com.example.checkcheck.dto.responseDto.SocialResponseDto;
import com.example.checkcheck.dto.responseDto.TokenFactory;
import com.example.checkcheck.dto.userinfo.KakaoUserInfoDto;
import com.example.checkcheck.model.Member;
import com.example.checkcheck.model.RefreshToken;
import com.example.checkcheck.repository.RefreshTokenRepository;
import com.example.checkcheck.repository.MemberRepository;
import com.example.checkcheck.security.UserDetailsImpl;
import com.example.checkcheck.service.MemberService;
import com.example.checkcheck.util.ComfortUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class SocialKakaoService {

    @Value("${cloud.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;
    @Value("${cloud.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ComfortUtils comfortUtils;



    //header 에 Content-type 지정
    //1번
    @Transactional
    public SocialResponseDto kakaoLogin(String code, HttpServletResponse response)
            throws JsonProcessingException {
        // 1. "인가코드" 로 "액세스 토큰" 요청
        String getAccessToken = getAccessToken(code);

        // 2. 토큰으로 카카오 API 호출
        KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(getAccessToken);

        // 3. 카카오ID로 회원가입 처리
        Member kakaoMember = signupKakaoUser(kakaoUserInfo);

        //4. 강제 로그인 처리
        forceLoginKakaoUser(kakaoMember, response);

        // User 권한 확인
        TokenFactory tokenFactory = memberService.accessAndRefreshTokenProcess(kakaoMember.getUserEmail(), response);

        SocialResponseDto socialResponseDto = SocialResponseDto.builder()
                .userEmail(kakaoMember.getUserEmail())
                .nickName(kakaoUserInfo.getNickname())
                .accessToken("Bearer "+tokenFactory.getAccessToken())
                .refreshToken(tokenFactory.getRefreshToken())
//                .jwtToken("Bearer "+jwtToken)

                .userRank(comfortUtils.getUserRank(kakaoMember.getPoint()))
                .build();

//        리프레시 토큰
        RefreshToken token = RefreshToken.builder()
                .key(socialResponseDto.getUserEmail())
                .value(tokenFactory.getRefreshToken())
                .build();
        refreshTokenRepository.save(token);

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
        body.add("client_id", kakaoClientId);
        body.add("redirect_uri", "http://localhost:8080/user/signin/kakao");
//        body.add("redirect_uri", "http://localhost:3000/user/signin/kakao");
        body.add("code", code);
        body.add("client_secret", clientSecret);


        //HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );
        //HTTP 응답 (JSON) -> 액세스 토큰 파싱
        //JSON -> JsonNode 객체로 변환
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        String accessToken=jsonNode.get("access_token").asText();

        return accessToken;
    }

    //2번
    public KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();

//        ResponseEntity<String> response = rt.postForEntity("https://kapi.kakao.com/v2/user/me",kakaoUserInfoRequest,String.class);

        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );
        //HTTP 응답 (JSON)
        //JSON -> JsonNode 객체로 변환
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties")
                .get("nickname").asText();
        String email = jsonNode.get("kakao_account")
                .get("email").asText();

//        String profileUrl = jsonNode.get("properties")
//                .get("profile_image").asText();
        return new KakaoUserInfoDto(id, nickname, email);
    }

    // 3번
    private Member signupKakaoUser(KakaoUserInfoDto kakaoUserInfoDto) {
        // 재가입 방지
        // DB 에 중복된 Kakao Id 가 있는지 확인
        Double kakaoId = Double.valueOf(kakaoUserInfoDto.getKakaoId());
        Member findKakao = memberRepository.findByUserEmail("k_"+kakaoUserInfoDto.getUserEmail())
                .orElse(null);

        //DB에 중복된 계정이 없으면 회원가입 처리
        if (findKakao == null) {
            String userName = kakaoUserInfoDto.getNickname();
            String email = kakaoUserInfoDto.getUserEmail();
            String password = UUID.randomUUID().toString();
            String encodedPassword = password;
            String provider = "kakao";
            LocalDateTime createdAt = LocalDateTime.now();

            Member kakaoMember = Member.builder()

                    .nickName(userName)
                    .userEmail("k_"+email)
                    .password(encodedPassword)
                    .userRealEmail(email)
//                    .userRank("Bronze")
//                    .createdAt(createdAt)
//                    .socialId(kakaoId)
                    .provider(provider)
                    .build();
            memberRepository.save(kakaoMember);


            return kakaoMember;
        }
        return findKakao;
    }

    // 4번
    public Authentication forceLoginKakaoUser(Member kakaoMember, HttpServletResponse response) {
        UserDetailsImpl userDetails = new UserDetailsImpl(kakaoMember);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authentication;
    }
}
