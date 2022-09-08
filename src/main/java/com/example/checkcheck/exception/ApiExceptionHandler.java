package com.example.checkcheck.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
TODO : Service단에서 활용하면 훨씬 처리가 수월할 것으로 예상 + Swagger
* FOR 글로벌 exception handling & custom exception handling
*/
@RestControllerAdvice //RestController의 예외처리에 대해서 AOP를 적용하기 위해 사용
public class ApiExceptionHandler extends RuntimeException{

    @ExceptionHandler(MethodArgumentNotValidException.class) //예외가 발생한 요청을 처리하기 위해
    public ResponseEntity<ErrorResponse> handleMethodNotValidException(MethodArgumentNotValidException e){
        final String[] message = {""};

        e.getBindingResult().getAllErrors()
                .forEach(c -> message[0] = c.getDefaultMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .msg(message[0])
                        .errorCode("400")
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .build()
                );
    }

    // 커스텀 예외처리
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        ErrorCode code = e.getCode();
        return ErrorResponse.of(code);
    }

    //TODO : 프론트분들을 위해 400 에러로 처리될 때 필요할 것을 대비함

    //전부 커스텀 Exception으로 변경 시 제거되도 되는 코드
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleArgumentException(IllegalArgumentException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .msg(e.getMessage())
                        .errorCode("400")
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .build()
                );
    }


    //전부 커스텀 Exception으로 변경 시 제거되도 되는 코드
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> usernameNotFoundException(UsernameNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .msg(e.getMessage())
                        .errorCode("400")
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .build()
                );
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> nullPointException(NullPointerException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .msg(e.getMessage())
                        .errorCode("400")
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .build()
                );
    }

}
