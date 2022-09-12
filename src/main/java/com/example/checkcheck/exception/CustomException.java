package com.example.checkcheck.exception;

import lombok.Getter;

//TODO: 사용법은
@Getter
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMsg());
        this.errorCode = errorCode;
    }

}
