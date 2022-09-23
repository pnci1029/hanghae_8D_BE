package com.example.checkcheck.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MyPageMemberResponseDto {
    private String nickName;
    private String userName;
    private String userEmail;
    private String userRank;
    private int userPoint;
    private int articleCount;
    private Boolean isAccepted;

}
