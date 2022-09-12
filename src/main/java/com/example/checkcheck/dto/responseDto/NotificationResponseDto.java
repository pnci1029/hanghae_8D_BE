package com.example.checkcheck.dto.responseDto;

import com.example.checkcheck.model.AlarmType;
import com.example.checkcheck.model.Notification;
import com.example.checkcheck.util.ComfortUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class NotificationResponseDto {

    private Long id;

    private String message;

    private String url;

    private Boolean status;

    @Builder
    public NotificationResponseDto(Long id, String message, String url, Boolean status) {
        this.id = id;
        this.message = message;
        this.url = url;
        this.status = status;
    }

    public static NotificationResponseDto create(Notification notification) {
        return NotificationResponseDto.builder()
                .id(notification.getId())
                .message(notification.getMessage())
                .url(notification.getUrl())
                .status(notification.getReadState())
                .build();
    }
}
