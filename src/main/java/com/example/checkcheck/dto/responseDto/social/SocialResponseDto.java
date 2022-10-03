package com.example.checkcheck.dto.responseDto.social;

import com.example.checkcheck.model.Member;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SocialResponseDto {

    private String nickName;
    private String userEmail;
    private String accessToken;
    private String refreshToken;
    private String userRank;
    private Boolean isAccepted;
    private Boolean alarmStatus;

    public SocialResponseDto(String nickName,
                             String userEmail,
                             String accessToken,
                             String refreshToken,
                             String userRank,
                             Boolean isAccepted,
                             Boolean alarmStatus) {
        this.nickName = nickName;
        this.userEmail = userEmail;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userRank = userRank;
        this.isAccepted = isAccepted;
        this.alarmStatus = alarmStatus;
    }
}
