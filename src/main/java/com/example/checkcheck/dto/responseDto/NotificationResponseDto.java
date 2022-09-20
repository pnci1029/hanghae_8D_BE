package com.example.checkcheck.dto.responseDto;

import com.example.checkcheck.model.AlarmType;
import com.example.checkcheck.model.Notification;
import com.example.checkcheck.util.ComfortUtils;
import com.example.checkcheck.util.Time;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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
        long now = ChronoUnit.MINUTES.between(notification.getCreatedAt() , LocalDateTime.now());
        Time time = new Time();
        String createdAt = time.times(now);

        return NotificationResponseDto.builder()
                .id(notification.getId())
                .message(notification.getMessage())
                .alarmType(notification.getAlarmType())
                .articlesId(notification.getUrl())
                .title(notification.getTitle())
                .readStatus(notification.getReadState())
                .createdAt(createdAt)
                .build();
    }
}
