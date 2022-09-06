package com.example.checkcheck.dto.responseDto;



import com.example.checkcheck.model.commentModel.Type;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
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
    @Size(max = 80)
    private String comment;
    private Boolean isSelected;
    private Boolean isMyComment;
    private String createdAt;
}
