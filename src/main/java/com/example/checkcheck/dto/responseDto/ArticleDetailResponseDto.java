package com.example.checkcheck.dto.responseDto;

import com.example.checkcheck.model.articleModel.Article;
import com.example.checkcheck.model.articleModel.Category;
import com.example.checkcheck.model.articleModel.Process;
import com.example.checkcheck.util.TimeStamped;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ArticleDetailResponseDto extends TimeStamped {

    private Long articlesId;
    private int price;
    private String nickName;
    private Process process;
    private String title;
    private String content;

    private String userRank;
    private List<String> images;
    private Boolean isMyArticles;
    private String category;

    @Builder
    public ArticleDetailResponseDto(Article article, List<String> image, Boolean isMyArticles, String category) {
        this.articlesId = article.getArticleId();
        this.price = article.getPrice();
        this.nickName = article.getNickName();
        this.process = article.getProcess();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.userRank = article.getUserRank();
        this.images = image;
        this.isMyArticles = isMyArticles;
        this.category = category;
    }
}
