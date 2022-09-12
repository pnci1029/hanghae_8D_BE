package com.example.checkcheck.dto.responseDto;

import com.example.checkcheck.model.articleModel.Article;
import com.example.checkcheck.model.articleModel.Process;
import lombok.Builder;
import lombok.Data;
import org.apache.tomcat.jni.Proc;

import java.text.NumberFormat;

@Data
public class ArticleResponseDto {

    private Long articlesId;
    private String price;
    private String nickName;
    private String process;
    private String title;
    private String userRank;
    private String image;
    private String selectedPrice;

    @Builder
    public ArticleResponseDto(Article article, String image, String userRank, String process, String selectedPrice, String price) {
        this.articlesId = article.getArticleId();
//        천단위 컴마찍기위해서 넣음
//        스트링으로 잘받는지 확인필요
        this.price = price;
        this.nickName = article.getNickName();
        this.process = process;
        this.title = article.getTitle();
        this.userRank = userRank;
        this.image = image;
        this.selectedPrice = selectedPrice;
    }

}
