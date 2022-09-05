package com.example.checkcheck.testForAlarm;

import com.example.checkcheck.model.articleModel.Article;
import com.example.checkcheck.util.TimeStamped;

import javax.persistence.*;

public class ArticleExample extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articlesExampleId;

    @Column
    private Long userEmail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id") //TODO : 예시로 JoinColumn 명 정해놓은 것
    private Article article;

    public ArticleExample(Long userEmail, Article article) {
        this.userEmail = userEmail;
        this.article = article;
    }
}
