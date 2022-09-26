package com.example.checkcheck.dto.responseDto;

import com.example.checkcheck.model.articleModel.Article;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ArticleDetailResponseDto {

    private Long articlesId;
    private String price;
    private String selectedPrice;
    private String nickName;
    private String process;
    private String title;
    private String content;

    private String userRank;
    private List<String> images;
    private Boolean isMyArticles;
    private String category;
    private String createdAt;

    @Builder
    public ArticleDetailResponseDto(Article article, List<String> image, Boolean isMyArticles,
                                    String category, String process, String price, String selectedPrice, String createdAt) {
        this.articlesId = article.getArticleId();
//        천단위 컴마찍기위해서 넣음
        this.price = price;
        this.selectedPrice = selectedPrice;
        this.nickName = article.getNickName();
        this.process = process;
        this.title = article.getTitle();
        this.content = article.getContent();
        this.userRank = article.getUserRank();
        this.images = image;
        this.isMyArticles = isMyArticles;
        this.category = category;
        this.createdAt = createdAt;
    }
}
