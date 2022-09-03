package com.example.checkcheck.dto.userinfo;

import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class KakaoUserInfoDto {
    private Long KakaoId;
    private String nickname;
    private String userEmail;
    private String profileUrl;
    private String message;
    private boolean response;

    public KakaoUserInfoDto(Long KakaoId, String nickname, String userEmail) {
        this.KakaoId = KakaoId;
        this.nickname = nickname;
        this.userEmail = userEmail;
    }
}