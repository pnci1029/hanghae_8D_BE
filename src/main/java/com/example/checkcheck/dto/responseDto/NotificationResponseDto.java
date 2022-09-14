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

    private Long notificationId;

    private String message;

    private Long articlesId;

    private Boolean readStatus;

    private AlarmType alarmType;

    private String title;

    private String createdAt;


    @Builder
    public NotificationResponseDto(Long id, String message, Long articlesId, Boolean readStatus,
                                   AlarmType alarmType, String title,String createdAt) {
        this.notificationId = id;
        this.message = message;
        this.articlesId = articlesId;
        this.readStatus = readStatus;
        this.title = title;
        this.alarmType = alarmType;
        this.createdAt = createdAt;
    }

    public static NotificationResponseDto create(Notification notification) {
        return NotificationResponseDto.builder()
                .id(notification.getId())
                .message(notification.getMessage())
                .alarmType(notification.getAlarmType())
                .articlesId(notification.getArticlesId())
                .title(notification.getTitle())
                .readStatus(notification.getReadState())
                .createdAt(createdAt)
                .build();
    }
}
