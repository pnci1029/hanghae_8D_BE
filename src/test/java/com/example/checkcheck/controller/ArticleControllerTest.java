//package com.example.checkcheck.controller;
//
//import com.example.checkcheck.dto.responseDto.ArticleResponseDto;
//import com.example.checkcheck.model.Member;
//import com.example.checkcheck.model.articleModel.Article;
//import com.example.checkcheck.model.articleModel.Process;
//import com.example.checkcheck.repository.ArticleRepository;
//import com.example.checkcheck.repository.MemberRepository;
//import com.example.checkcheck.service.ArticleServiceTest;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Slice;
//
//import javax.transaction.Transactional;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
//
//@SpringBootTest
////order 순서대로 실행
//@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
//public class ArticleControllerTest {
//
//    @Autowired
//    ArticleServiceTest articleServiceTest;
//    @Autowired
//    MemberRepository memberRepository;
//
//    @Autowired
//    ArticleRepository articleRepository;
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
//    @Test
//    @Order(1)
//    void 게시글_작성_O() {
//        String title = "타이틀타이틀";
//        String content = "내용내용내용";
//        String category = "전체";
//        int price = 10000;
//
//        Member member1 = memberRepository.findByNickName("유저1").orElse(null);
//
////        Article result = new ArticleServiceTest().create(title, content, category, price,member1);
//
//        List<Article> result =
//                articleServiceTest.create(title, content, category, price, member1);
//        assertThat(result.get(0).getNickName()).isEqualTo("유저1");
//        assertThat(result.size()).isEqualTo(20);
//    }
//
//    @Test
//    @Order(2)
//    void 게시글_작성_실패() {
////        위에  데이터 저장되서 꺼내온거
//        Member member1 = memberRepository.findById(1L).orElse(null);
//        String title = "";
//        String content = "내용내용내용";
//        String category = "전체";
//        int price = 10000;
//
////        예외처리 테스트
//        assertThatIllegalArgumentException().
//                isThrownBy(() -> articleServiceTest.create(title, content, category, price, member1));
//    }
//
//    @Test
//    @Order(3)
//    void 게시글_캐러셀_조회_O() {
//        List<ArticleResponseDto> result = articleServiceTest.getCarousel();
//        Collections.shuffle(result);
//
////        최종 리턴 리스트
//        List<ArticleResponseDto> realResult = new ArrayList<>();
//        if (result.size() < 10) {
//            realResult.addAll(result);
//        } else {
//            for (int i = 0; i < 10; i++) {
//                realResult.add(result.get(i));
//            }
//        }
//        assertThat(realResult.size()).isEqualTo(10);
//    }
//
//    @Test
//    @Order(4)
//    void 게시글_전체_조회_O() {
//        String category = "전체";
//        Process process = Process.process;
//        Slice<?> result = articleServiceTest.getAll(Pageable.ofSize(10), category, process);
//
//    }
//
//}
