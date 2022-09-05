package com.example.checkcheck.dto.responseDto;

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


}
