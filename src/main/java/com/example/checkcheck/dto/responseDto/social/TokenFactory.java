package com.example.checkcheck.dto.responseDto.social;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TokenFactory {

    private String accessToken;
    private String refreshToken;
    private String newAccessToken;

    @Builder
    public TokenFactory(String newAccessToken) {
        this.newAccessToken = newAccessToken;
    }

    @Builder
    public TokenFactory(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
