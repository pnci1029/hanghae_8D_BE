package com.example.checkcheck.dto.userinfo;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class NaverUserInfoDto {

    private String SocialId;
    private String nickname;
    private String userEmail;
    private String accessToken;
    private String refreshToken;

    @Builder
    public NaverUserInfoDto(String SocialId, String nickname, String userEmail, String accessToken,  String refreshToken) {
        this.SocialId = SocialId;
        this.nickname = nickname;
        this.userEmail = userEmail;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
