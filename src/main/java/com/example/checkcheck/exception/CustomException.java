package com.example.checkcheck.exception;

import lombok.Getter;

//TODO: 사용법은
@Getter
public class CustomException extends RuntimeException {

    private final ErrorCode code;

    public CustomException(ErrorCode code) {
        this.code = code;
    }

}
