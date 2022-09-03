package com.example.checkcheck.service.social;

import com.example.checkcheck.dto.responseDto.SocialResponseDto;
import com.example.checkcheck.dto.userinfo.NaverUserInfoDto;
import com.example.checkcheck.model.Member;
import com.example.checkcheck.model.RefreshToken;
import com.example.checkcheck.repository.RefreshTokenRepository;
import com.example.checkcheck.repository.UserRepository;
import com.example.checkcheck.security.UserDetailsImpl;
import com.example.checkcheck.service.UserService;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.UUID;
@RequiredArgsConstructor
@Service
public class SocialNaverSerivce {


    @Value("${cloud.security.oauth2.client.registration.naver.client-id}")
    private String client_id;
    @Value("${cloud.security.oauth2.client.registration.naver.client-secret}")
    private String clientSecret;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public SocialResponseDto naverLogin(String code, String state, HttpServletResponse response) {

        try {
            // 네이버에서 가져온 유저정보 + 임의 비밀번호 생성
            NaverUserInfoDto naverUser = getNaverUserInfo(code, state);
            String password = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(password);
            String provider = "naver";
            String naverUserEmail = naverUser.getUserEmail().substring(1, naverUser.getUserEmail().length() - 1);
            String realEmail = "n_" + naverUserEmail;


            // 재가입 방지
            // 네이버 ID로 유저 정보 DB 에서 조회
            Member member = userRepository.findByUserEmail(realEmail).orElse(null);


            // 없으면 회원가입
            if (member == null) {
                member = Member.builder()
                        .nickName(naverUser.getNickname().substring(1, naverUser.getNickname().length() - 1))
                        .password(encodedPassword)
                        .userEmail("n_"+naverUserEmail)
                        .userRealEmail(naverUser.getUserEmail().substring(1, naverUser.getUserEmail().length() - 1))
                        .userRank("Bronze")
//                        .socialId(Double.valueOf(naverUser.getNaverId().substring(1, naverUser.getNaverId().length() - 1)))
                        .createdAt(LocalDateTime.now())
                        .provider(provider)
                        .build();
                userRepository.save(member);

            } else {
                // 강제 로그인
                UserDetailsImpl userDetails = new UserDetailsImpl(member);
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }


            System.out.println("결과는"+ member.getUserEmail());
            // User 권한 확인


//            토큰 관리
            String jwtToken = userService.accessAndRefreshTokenProcess(member.getUserEmail(), response);

            String accessToken = naverUser.getAccessToken();
            String refreshToken = naverUser.getRefreshToken();


//            리프레시 토큰 저장
            RefreshToken token = new RefreshToken(member.getUserEmail(), refreshToken);
            refreshTokenRepository.save(token);

            SocialResponseDto socialResponseDto = SocialResponseDto.builder()
                    .nickName(member.getNickName()) // 1
                    .userEmail(member.getUserEmail())
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .userRank(member.getUserRank())
                    .refreshToken(refreshToken)
                    .jwtToken("Bearer "+jwtToken)
                    .build();
            return socialResponseDto;

        } catch (IOException e) {
            return null;
        }
    }


    // 네이버에 요청해서 데이터 전달 받는 메소드
    public JsonElement jsonElement(String reqURL, String token, String code, String state) throws IOException {

        // 요청하는 URL 설정
        URL url = new URL(reqURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // POST 요청을 위해 기본값이 false인 setDoOutput을 true로
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        // POST 요청에 필요한 데이터 저장 후 전송
        if (token == null) {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            String sb = "grant_type=authorization_code" +
                    "&client_id="+client_id +
                    "&client_secret="+clientSecret +
                    "&redirect_uri=http://localhost:8080/user/signin/naver" +
                    "&code=" + code +
                    "&state=" + state;
            bw.write(sb);
            bw.flush();
            bw.close();
        } else {
            conn.setRequestProperty("Authorization", "Bearer " + token);
        }

        // 요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        StringBuilder result = new StringBuilder();

        while ((line = br.readLine()) != null) {
            result.append(line);
        }
        br.close();

        // Gson 라이브러리에 포함된 클래스로 JSON 파싱
        return JsonParser.parseString(result.toString());
    }


    // 네이버에 요청해서 회원정보 받는 메소드
    public NaverUserInfoDto getNaverUserInfo(String code, String state) throws IOException {

        String codeReqURL = "https://nid.naver.com/oauth2.0/token";
        String tokenReqURL = "https://openapi.naver.com/v1/nid/me";


        // 코드를 네이버에 전달하여 엑세스 토큰 가져옴
        JsonElement tokenElement = jsonElement(codeReqURL, null, code, state);
        String access_Token = tokenElement.getAsJsonObject().get("access_token").getAsString();
        String refresh_token = tokenElement.getAsJsonObject().get("refresh_token").getAsString();

        // 엑세스 토큰을 네이버에 전달하여 유저정보 가져옴
        JsonElement userInfoElement = jsonElement(tokenReqURL, access_Token, null, null);
        String naverId = String.valueOf(userInfoElement.getAsJsonObject().get("response")
                .getAsJsonObject().get("id"));
        String userEmail = String.valueOf(userInfoElement.getAsJsonObject().get("response")
                .getAsJsonObject().get("email"));
        String nickName = String.valueOf(userInfoElement.getAsJsonObject().get("response")
                .getAsJsonObject().get("name"));

//        String email = "n_" + userEmail;
        return new NaverUserInfoDto(naverId, nickName, userEmail, access_Token, refresh_token);
    }

}
