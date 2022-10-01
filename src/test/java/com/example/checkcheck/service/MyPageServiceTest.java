//package com.example.checkcheck.service;
//
//import com.example.checkcheck.model.Member;
//import com.example.checkcheck.repository.MemberRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.MethodOrderer;
//import org.junit.jupiter.api.TestMethodOrder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import javax.transaction.Transactional;
//import java.time.LocalDateTime;
////order 순서대로 실행
//@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
//@SpringBootTest
//@Transactional
//class MyPageServiceTest {
//
//    @Autowired
//    MemberRepository memberRepository;
//    @Autowired
//    MyPageService myPageService;
//
//    @BeforeEach
//    public void before() {
//        Member member1 = Member.builder()
//                .userName("유저1")
//                .nickName("유저1")
//                .createdAt(LocalDateTime.now())
//                .isDeleted(false)
//                .isAccepted(false)
//                .password("1234")
//                .provider("naver")
//                .userEmail("n_test1@naver.com")
//                .build();
//        Member member2 = Member.builder()
//                .userName("유저2")
//                .nickName("유저2")
//                .createdAt(LocalDateTime.now())
//                .isDeleted(false)
//                .isAccepted(false)
//                .password("12345")
//                .provider("naver")
//                .userEmail("n_test2@naver.com")
//                .build();
//        memberRepository.save(member1);
//        memberRepository.save(member2);
//    }
//
//    void 마이페이지_회원정보_조회() {
//    }
//
//}