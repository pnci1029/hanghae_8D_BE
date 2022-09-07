package com.example.checkcheck.model;

import com.example.checkcheck.exception.CustomException;
import com.example.checkcheck.exception.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor
public class NotificationMessage {

    private static final int Max_LENGTH = 50;

    @Column(nullable = false,length = Max_LENGTH)
    private String message;

    public NotificationMessage(String message){
        if(isNotValidNotificationMessage(message)){
            throw new CustomException(ErrorCode.NOT_VALIDMESSAGE);
        }
        this.message = message;
    }

    private boolean isNotValidNotificationMessage(String message) {
        return Objects.isNull(message) || message.length() > Max_LENGTH
                || message.isEmpty();

    }
}
