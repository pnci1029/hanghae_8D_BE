package com.example.checkcheck.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CommentListResponseDto {
    private Boolean isMyArticles;
    private List<CommentResponseDto> comments;

}
