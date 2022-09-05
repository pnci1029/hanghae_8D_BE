package com.example.checkcheck.dto.responseDto;



import com.example.checkcheck.model.commentModel.Type;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommentResponseDto {

    private Long commentId;
    private Type type;
    private String nickName;
    private String userRank;
    private String comment;

    @JsonFormat(pattern = "yyyy.MM.dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
}
