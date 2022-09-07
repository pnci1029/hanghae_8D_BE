package com.example.checkcheck.dto.responseDto;

import com.example.checkcheck.model.Image;
import com.example.checkcheck.model.articleModel.Article;
import com.example.checkcheck.model.articleModel.Process;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class ArticleResponseDto {

    private Long articlesId;
    private int price;
    private String nickName;
    private Process process;
    private String title;
    private String userRank;
    private String image;

    @Builder
    public ArticleResponseDto(Article article, String image,String userRank) {
        this.articlesId = article.getArticleId();
        this.price = article.getPrice();
        this.nickName = article.getNickName();
        this.process = article.getProcess();
        this.title = article.getTitle();
        this.userRank = userRank;
        this.image = image;
    }

}
