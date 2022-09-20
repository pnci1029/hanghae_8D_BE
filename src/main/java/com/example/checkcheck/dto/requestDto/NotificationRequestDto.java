package com.example.checkcheck.dto.requestDto;

import com.example.checkcheck.model.AlarmType;
import com.example.checkcheck.model.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * 알람 요청을 위한 requestDto
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequestDto {

    private Member receiver;
    private AlarmType alarmType;
    private String content;
    private String url;

}
