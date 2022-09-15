//package com.example.checkcheck.controller;
//
//import com.example.checkcheck.exception.CustomException;
//import com.example.checkcheck.exception.ErrorCode;
//import com.example.checkcheck.model.Member;
//import com.example.checkcheck.repository.MemberRepository;
//import com.example.checkcheck.security.JwtTokenProvider;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.context.WebApplicationContext;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
//@Transactional
//public class NotificationControllerTest {
//
//    //TODO: 알람 프론트 -> 백엔드 구독 테스트하기
//
//    @Autowired
//    private MockMvc mockMvc;
//    @Autowired
//    WebApplicationContext context;
//    @Autowired
//    MemberRepository memberRepository;
//    @Autowired
//    JwtTokenProvider jwtTokenProvider;
//    @Autowired
//    NotificationController notificationController;
//
//    @Test
//    @DisplayName("view가 없으니 test로 SSE 연결을 확인한다")
//    public void subscribe() throws Exception {
//        //given
//        try {
//            Long memberId = 5L;
//            Member member = memberRepository.findById(memberId).orElseThrow(
//                    () -> new CustomException(ErrorCode.USER_NOT_FOUND));
//            String accessToken = String.valueOf(jwtTokenProvider.createToken(String.valueOf(member)));
//
//            //when - > then
//            mockMvc.perform(MockMvcRequestBuilders.get("/api/subscribe")
//                    .header("Authorizaiton", accessToken)
//                    .accept(MediaType.TEXT_EVENT_STREAM));
//        } catch (NullPointerException e) {
//            throw new IllegalArgumentException("어떤 오류가 발생함");
//        }
//        // TODO : 0910 수정필요!!!
//    }
//}