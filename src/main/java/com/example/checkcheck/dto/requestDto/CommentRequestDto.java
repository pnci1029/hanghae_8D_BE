package com.example.checkcheck.dto.requestDto;

import com.example.checkcheck.model.commentModel.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommentRequestDto {

    private Type type;
    private String comment;
}
