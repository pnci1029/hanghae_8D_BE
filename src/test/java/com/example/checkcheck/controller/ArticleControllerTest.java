package com.example.checkcheck.controller;

import com.example.checkcheck.model.Member;
import com.example.checkcheck.model.articleModel.Article;
import com.example.checkcheck.model.articleModel.Category;
import com.example.checkcheck.model.articleModel.Process;
import com.example.checkcheck.service.ArticleService;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ArticleControllerTest {

    @Autowired
    ArticleService articleService;


    @Test
    void 게시글_작성_테스트() {

        Member testUser = new Member("1234", "1234121","k_1234@naver.com", LocalDateTime.now(),"true", "1234@gmail.com");

        Article article = Article.builder()
                .userEmail("1234@naver.com")
                .member(testUser)
                .userRank("S")
                .process(Process.process)
                .category(Category.clothes)
                .content("게시글")
                .images(null)
                .nickName("호창형")
                .price(50000)
                .selectedPrice(0)
                .title("제목")
                .build();

        assertEquals(article.getNickName(),"홍창형");

    }
}