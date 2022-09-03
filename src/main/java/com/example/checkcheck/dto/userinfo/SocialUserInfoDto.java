package com.example.checkcheck.dto.userinfo;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class SocialUserInfoDto {
    private Long socialId;
    private String nickname;
    private String userEmail;
    private String accessToken;
    private String refreshToken;

    @Builder
    public SocialUserInfoDto(Long socialId, String nickname, String userEmail,String accessToken, String refreshToken) {
        this.socialId = socialId;
        this.nickname = nickname;
        this.userEmail = userEmail;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }


}
