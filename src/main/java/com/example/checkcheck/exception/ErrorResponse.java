package com.example.checkcheck.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResponse {

    private HttpStatus status;
    private String errorCode;
    private String errorMessage;

    @Builder
    public ErrorResponse(ErrorCode errorCode) {
        this.status = errorCode.getHttpStatus();
        this.errorCode = errorCode.getErrorCode();
        this.errorMessage = errorCode.getMsg();
    }
}
