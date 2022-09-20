package com.example.checkcheck.dto.responseDto;

import com.example.checkcheck.model.articleModel.Article;
import com.example.checkcheck.model.articleModel.Process;
import com.example.checkcheck.model.commentModel.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentChoiseResponseDto {

    private Long articlesId;
    private String process;
    private String commentsNickName;
    private String selectedPrice;
    private String commentsUserRank;
    private Boolean isSelected;
    private Boolean isMyComment;

    @Builder
    public CommentChoiseResponseDto(Article article, Comment comment, String process, String commentsUserRank, Boolean isSelected, Boolean isMyComment) {
        this.articlesId = article.getArticleId();
        this.process = process;
        this.commentsNickName = comment.getArticle().getNickName();
        this.selectedPrice = comment.getComment();
        this.commentsUserRank = commentsUserRank;
        this.isSelected = isSelected;
        this.isMyComment = isMyComment;
    }
}
