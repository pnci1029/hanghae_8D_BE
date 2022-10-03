//package com.example.checkcheck.controller;
//
//import com.example.checkcheck.dto.responseDto.ArticleResponseDto;
//import com.example.checkcheck.model.Member;
//import com.example.checkcheck.model.articleModel.Article;
//import com.example.checkcheck.model.articleModel.Process;
//import com.example.checkcheck.model.commentModel.Comment;
//import com.example.checkcheck.model.commentModel.Type;
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
//import static org.assertj.core.api.Assertions.*;
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
//        List<Article> all = articleRepository.findAll();
//        System.out.println("all = " + all.get(0).getUserEmail());
//    }
//
//    @Test
//    @Order(5)
//    void 게시글_수정_X() {
////        수정될 내용
//        String title = "";
//        String content = "수정내용";
//        int price = 5555;
//        String category = "전체";
//        assertThatNullPointerException()
//                .isThrownBy(() -> articleServiceTest.patch(1L, title, content, price, category));
//    }
//
//    @Test
//    @Order(6)
//    void 게시글_수정_O() {
////        수정될 내용
//        String title = "수정제목";
//        String content = "수정내용";
//        int price = 5555;
//        String category = "전체";
//        Article result = articleServiceTest.patch(1L, title, content, price, category);
//        assertThat(result.getTitle()).isEqualTo("수정제목");
//    }
//
//    @Test
//    @Transactional
//    @Order(7)
//    void 게시글_삭제_X() {
//        assertThatNullPointerException()
//                .isThrownBy(() -> articleServiceTest.delete(50L));
//    }
//
//    @Test
//    @Transactional
//    @Order(8)
//    void 게시글_삭제_O() {
//        String result = articleServiceTest.delete(2);
//        assertThat(result).isEqualTo("삭제가 완료되었습니다.");
//    }
////ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ댓글ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ//
////ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ댓글ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ//
////ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ댓글ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ//
//
//    @Test
//    @Order(9)
//    void 댓글_작성_X_게시글없음() {
//        String comment = "댓글댓글";
//        Type type = Type.text;
//        assertThatNullPointerException()
//                .isThrownBy(() -> articleServiceTest.createComment(50L, comment, type));
//    }
//
//    @Test
//    @Order(9)
//    void 댓글_작성_X_내용없음() {
//        String comment = "";
//        Type type = Type.text;
//        assertThatIllegalArgumentException()
//                .isThrownBy(() -> articleServiceTest.createComment(1L, comment, type));
//    }
//
//    @Test
//    @Order(10)
//    void 댓글_작성_O() {
//        String comment = "댓글댓글";
//        Type type = Type.text;
//        Comment comments = articleServiceTest.createComment(1L, comment, type);
//        assertThat(comments.getComment()).isEqualTo("댓글댓글");
//    }
//
//    @Test
//    @Order(11)
//    void 댓글_조회_X_게시글_넘버() {
//        assertThatNullPointerException()
//                .isThrownBy(() -> articleServiceTest.allComment(50L));
//    }
//
//    @Test
//    @Order(12)
//    void 댓글_조회_O() {
//        List<Comment> comment = articleServiceTest.allComment(1L);
//        assertThat(comment.size()).isEqualTo(10);
//    }
//}
