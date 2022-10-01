//package com.example.checkcheck.controller;
//
//import com.example.checkcheck.dto.requestDto.ArticleRequestDto;
//import com.example.checkcheck.dto.responseDto.ArticleResponseDto;
//import com.example.checkcheck.dto.responseDto.ResponseDto;
//import com.example.checkcheck.model.Member;
//import com.example.checkcheck.model.articleModel.Process;
//import com.example.checkcheck.repository.MemberRepository;
//import com.example.checkcheck.security.UserDetailsImpl;
//import com.example.checkcheck.service.ArticleService;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Slice;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
////order 순서대로 실행
//@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
//class ArticleControllerTest1 {
//
//    @Autowired
//    MemberRepository memberRepository;
//    @Autowired
//    ArticleService articleService;
//    MockMvc mockMvc;
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
//
//    }
//
//    @Order(1)
//    @Test
//    void 게시글_작성() throws IOException {
//
//        Member member1 = memberRepository.findByUserName("유저1").orElse(null);
//        Member member2 = memberRepository.findByUserName("유저2").orElse(null);
//
//        System.out.println("member1 = " + member1.getUserEmail());
//        System.out.println("member2 = " + member2.getUserEmail());
//        ArticleRequestDto articleRequestDto = new ArticleRequestDto();
//        articleRequestDto.setCategory("전체");
//        articleRequestDto.setContent("내용내용내용");
//        articleRequestDto.setPrice(10000);
//        articleRequestDto.setTitle("제목제목");
//        articleRequestDto.setImageList(null);
//
////        테스트 게시글1
//        ResponseDto<?> result = articleService.postArticles(null, articleRequestDto, new UserDetailsImpl(member1));
//
//        ArticleRequestDto articleRequestDto2 = new ArticleRequestDto();
//        articleRequestDto2.setCategory("전체");
//        articleRequestDto2.setContent("내용내용내용2");
//        articleRequestDto2.setPrice(20000);
//        articleRequestDto2.setTitle("제목제목2");
//        articleRequestDto2.setImageList(null);
//
////        테스트 게시글2
//        articleService.postArticles(null, articleRequestDto2,new UserDetailsImpl(member2));
//
//        assertThat(result.isSuccess()).isTrue();
//
//    }
//
//    @Order(2)
//    @Test
//    void 게시글_전체_조회() {
//        Slice<ArticleResponseDto> result = articleService.getAllArticles(Pageable.ofSize(10), "전체", Process.process);
//        System.out.println("result = " + result.getContent().get(0).getTitle());
//        assertThat(result.getContent().get(0).getTitle()).isEqualTo("제목제목2");
//    }
//
//    @Order(3)
//    @Test
//    void 게시글_캐러셀_조회() {
//        ResponseDto<List<ArticleResponseDto>> result = articleService.getArticleCarousel();
//        assertThat(result.getData().size()).isEqualTo(2);
//    }
//
//
//}