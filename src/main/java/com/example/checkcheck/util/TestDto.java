package com.example.checkcheck.util;

import com.example.checkcheck.dto.responseDto.NotificationResponseDto;
import lombok.Builder;

@Builder
public class TestDto {
    private NotificationResponseDto notificationResponseDtoList;
    private String createdAt;

}
