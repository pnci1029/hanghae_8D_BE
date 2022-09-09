package com.example.checkcheck.dto.responseDto;

import com.example.checkcheck.model.articleModel.Article;
import com.example.checkcheck.model.articleModel.Category;
import com.example.checkcheck.model.articleModel.Process;
import com.example.checkcheck.util.TimeStamped;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ArticleDetailResponseDto extends TimeStamped {

    private Long articlesId;
    private String price;
    private String nickName;
    private String process;
    private String title;
    private String content;

    private String userRank;
    private List<String> images;
    private Boolean isMyArticles;
    private String category;

    @Builder
    public ArticleDetailResponseDto(Article article, List<String> image, Boolean isMyArticles, String category, String process) {
        this.articlesId = article.getArticleId();
//        천단위 컴마찍기위해서 넣음
        this.price = NumberFormat.getInstance().format(article.getPrice());
        this.nickName = article.getNickName();
        this.process = process;
        this.title = article.getTitle();
        this.content = article.getContent();
        this.userRank = article.getUserRank();
        this.images = image;
        this.isMyArticles = isMyArticles;
        this.category = category;
    }
}
