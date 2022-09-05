package com.example.checkcheck.dto.requestDto;

import com.example.checkcheck.model.AlarmType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor

/**
 * 알람 요청을 위한 requestDto
 */

public class NotificationRequestDto implements Serializable {
    private AlarmType alarmType;
    private String message;
}
