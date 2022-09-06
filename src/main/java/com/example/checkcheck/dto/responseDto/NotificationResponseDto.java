package com.example.checkcheck.dto.responseDto;

import com.example.checkcheck.model.AlarmType;
import com.example.checkcheck.model.Notification;
import com.example.checkcheck.util.ComfortUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NotificationResponseDto {

    private AlarmType alarmType;
    private String message;
    private boolean readState;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private String createdDate;

    @Builder
    public NotificationResponseDto(Notification notification, String createdDate) {
        this.alarmType = notification.getAlarmType();
        this.message = notification.getMessage();
        this.readState = notification.isReadState();
        this.createdDate = createdDate;
    }
}
