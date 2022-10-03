//package com.example.checkcheck.service;
//
//import com.example.checkcheck.dto.responseDto.ArticleResponseDto;
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
//import java.util.ArrayList;
//import java.util.List;
//
//@SpringBootTest
//@Service
//public class ArticleServiceTest {
//    @Autowired
//    ArticleRepository articleRepository;
//
//    public List<Article> create(String title, String content, String category, int price, Member member) {
//        if (title == null || title.isEmpty()) {
//            throw new IllegalArgumentException("제목을 입력해주세요.");
//        }
//        List<Article> result = new ArrayList<>();
//        for (int i = 0; i < 20; i++) {
//            Article article = Article.builder()
//                    .nickName(member.getNickName())
//                    .title(title)
//                    .content(content)
//                    .price(price)
//                    .category(category)
//                    .process(Process.process)
//                    .userRank("S")
//                    .member(member)
//                    .userEmail(member.getUserEmail())
//                    .selectedPrice(0)
//                    .build();
//            articleRepository.save(article);
//            result.add(article);
//        }
//        return result;
//    }
//
//    public Slice<?> getAll(Pageable pageable, String category, Process process) {
//
//        return null;
//    }
//
//    public List<ArticleResponseDto> getCarousel() {
////        진행중인 상태만 조회
//        List<ArticleResponseDto> resultBox = new ArrayList<>();
//        List<Article> result = articleRepository.findAll();
//        for (Article article : result) {
//            ArticleResponseDto articleResponseDto = ArticleResponseDto.builder()
//                    .article(article)
//                    .process(String.valueOf(Process.process))
//                    .userRank("S")
//                    .price(String.valueOf(article.getPrice()))
//                    .selectedPrice("0")
//                    .image(null)
//                    .build();
//            resultBox.add(articleResponseDto);
//        }
//        return resultBox;
//    }
//}
