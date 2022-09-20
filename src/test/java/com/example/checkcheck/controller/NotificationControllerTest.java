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
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.context.WebApplicationContext;
//
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
//@Transactional
////@WebMvcTest(NotificationController.class)
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
//    @DisplayName("SSE에 연결을 진행한다.")
//    public void subscribe() throws Exception {
//        //given
//        Long userId = 5L;
//        Member member = memberRepository.findById(userId).orElseThrow(
//                () -> new CustomException(ErrorCode.NullPoint_Token)
//        );
//        String accessToken = String.valueOf(jwtTokenProvider.createToken(String.valueOf(member)));
//
//        //when, then
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/subscribe")
//                        .header("Authorization", accessToken))
//                .andExpect(status().isOk());
//    }
//        // TODO : 0910 수정필요!!!
//}
//
