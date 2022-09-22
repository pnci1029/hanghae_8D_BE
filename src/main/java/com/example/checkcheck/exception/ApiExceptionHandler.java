package com.example.checkcheck.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@SuppressWarnings("unchecked")
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = { CustomException.class })
    public ResponseEntity<Object> handleApiRequestException(CustomException ex) {
        HttpStatus status = ex.getErrorCode().getHttpStatus();
        String errCode = ex.getErrorCode().getErrorCode();
        String errMSG = ex.getErrorCode().getMsg();
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(status);
        errorResponse.setErrorCode(errCode);
        errorResponse.setErrorMessage(errMSG);

        return new ResponseEntity(
                errorResponse,
                ex.getErrorCode().getHttpStatus()
        );
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> nullPointException(NullPointerException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .errorCode(ErrorCode.NullPoint_Token)
                        .build()
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorResponse> MethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(CustomErrorResponse.builder()
                        .errorCode("400")
                        .errorMessage(e.getBindingResult().getAllErrors().get(0).getDefaultMessage())
                        .status(HttpStatus.BAD_REQUEST)
                        .build()
                );
    }

}
