package com.example.checkcheck.dto.requestDto;

import com.example.checkcheck.model.articleModel.Article;
import com.example.checkcheck.model.commentModel.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommentRequestDto {
    private Long articlesId;
    private Type type;
    @Size(min = 1, max = 80, message = "댓글은 1~80자로 작성이 가능합니다.")
    private String comment;


}
