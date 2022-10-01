//package com.example.checkcheck.service;
//
//import com.example.checkcheck.dto.responseDto.ArticleResponseDto;
//import com.example.checkcheck.exception.CustomException;
//import com.example.checkcheck.exception.ErrorCode;
//import com.example.checkcheck.model.Member;
//import com.example.checkcheck.model.articleModel.Article;
//import com.example.checkcheck.model.articleModel.Process;
//import com.example.checkcheck.repository.ArticleRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Slice;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@SpringBootTest
//@Service
//public class ArticleServiceTest {
//    @Autowired
//    ArticleRepository articleRepository;
//
//    public Article create(String title, String content, String category, int price, Member member) {
//        if (title == null || title.isEmpty()) {
//            throw new IllegalArgumentException("제목을 입력해주세요.");
//        }
//        Article article = Article.builder()
//                .nickName(member.getNickName())
//                .title(title)
//                .content(content)
//                .price(price)
//                .category(category)
//                .process(Process.process)
//                .userRank("S")
//                .member(member)
//                .userEmail(member.getUserEmail())
//                .selectedPrice(0)
//                .build();
////        articleRepository.save(article);
//        return article;
//    }
//
//    public Slice<?> getAll(Pageable pageable, String category, Process process) {
//
//        return null;
//    }
//
//    public List<ArticleResponseDto> getCarousel() {
////        진행중인 상태만 조회
//        List<ArticleResponseDto> articleResponseDtos = articleRepository.articleCarousel();
//        return articleResponseDtos;
//    }
//}
