package com.example.checkcheck.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


/**
* exception 처리를 위해 임의로 만들어두었습니다.
* TODO: 임시방편으로 만들어두었기 때문에 나중에 우리 서비스에 맞춰서 변경 필요
*  추후 모든 Controller 형식을 ResponseEntity 형식으로 변경해주기만 하면 됩니다 *
*/

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    OK(HttpStatus.OK,  "200", "true"),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500", "서버가 다운되었습니다, 잠시후 다시 접속해주세요"),
    // 회원가입 + 로그인 + 이메일 체크
    DUPLE_EMAIL(HttpStatus.BAD_REQUEST, "400", "중복된 이메일 입니다."),
    EMAIL_CONTENT_END(HttpStatus.BAD_REQUEST, "400", ""),
    INCORRECT_EMAIL_CODE(HttpStatus.BAD_REQUEST, "400", "이메일 인증에 실패하였습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "사용자를 찾을 수 없습니다."),
    CONFIRM_EMAIL_PWD(HttpStatus.BAD_REQUEST, "400", "이메일 또는 비밀번호를 확인해주세요."),

    INVALID_OLD_PWD(HttpStatus.BAD_REQUEST, "400", "기존 비밀번호가 옳바르지 않습니다."),

    NOT_LOGIN(HttpStatus.BAD_REQUEST, "400", "로그인이 필요합니다."),
    UNKNOWN_ERROR(HttpStatus.UNAUTHORIZED,"403", "토큰이 존재하지 않습니다1."),

    NOT_EXPIRED_TOKEN_YET(HttpStatus.BAD_REQUEST,"400", "토큰이 만료되지 않았습니다."),
    EXPIRED_TOKEN(HttpStatus.BAD_REQUEST,"401", "만료된 토큰입니다."),
    ACCESS_DENIED(HttpStatus.UNAUTHORIZED,"401", "유효한 토큰이 아닙니다."),

    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST,"400","유효하지 않은 리프레시 토큰입니다."),

    NEED_EMAIL(HttpStatus.BAD_REQUEST,"400","이메일은 필수로 동의 해주셔야 합니다."),

    FILE_NULL(HttpStatus.BAD_REQUEST, "400", "파일을 입력해주세요."),
    UnSupported_Token(HttpStatus.UNAUTHORIZED, "401", "올바른 JWT 토큰을 입력해주세요"),
    Signature_Exception(HttpStatus.BAD_REQUEST, "404", "변조된 JWT 토큰입니다"),

    // 알림 URL, MESSAGE
    NOT_VALIDURL(HttpStatus.BAD_REQUEST,"400","유효하지 않는 URL 입니다."),
    NOT_VALIDMESSAGE(HttpStatus.BAD_REQUEST,"400","유효하지 않는 내용입니다."),
    NOT_EXIST_NOTIFICATION(HttpStatus.BAD_REQUEST,"400","존재하지 않는 알림입니다. 새로고침 해주세요."),

    // 이메일 전송
    USER_ALREADY_REJECTED_EMAIL(HttpStatus.BAD_REQUEST, "400", "유저가 이메일 수신을 이미 거절했습니다"),

    // 게시글
    ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "존재하지 않는 게시글입니다. 다시 시도해주세요"),
    NO_IMAGE_EXCEPTION(HttpStatus.BAD_REQUEST, "406", "이미지를 추가해주세요"),


    //댓글
    TOO_MUCH_COMMENTS(HttpStatus.BAD_REQUEST, "400", "댓글은 10개까지 작성할 수 있습니다."),
    IS_ALREADY_CHOSEN(HttpStatus.BAD_REQUEST, "400", "이미 채택이 완료된 게시물입니다. 새로고침 해주세요."),

    //마이페이지




    // 정렬 옵션,
    INVALID_SORTING_OPTION(HttpStatus.BAD_REQUEST, "400", "잘못된 sort 옵션입니다"),
    INVALID_FILTER_OPTION(HttpStatus.BAD_REQUEST, "400", "잘못된 filter 옵션입니다"),
    SUBSCRIBE_ERROR(HttpStatus.BAD_REQUEST, "400", "잘못된 Subscribe 요청입니다"),

    EXIST_NICKNAME(HttpStatus.BAD_REQUEST, "400","이미 사용 중인 닉네임입니다.\n 다시 입력해주세요"),
    NICKNAME_EXCEPTION(HttpStatus.BAD_REQUEST,"400" ,"닉네임은 1글자 이상 6글자 이하로 작성해주세요" ),
    NICKNAME_TYPE_EXCEPTION(HttpStatus.BAD_REQUEST,"400" ,"닉네임은 한글, 영어, 숫자만 입력할 수 있습니다."),



    WRONG_CODE_EXCEPTION(HttpStatus.BAD_REQUEST, "400","인가코드가 유효하지 않습니다." ),
    EXPIRE_REFRESH_TOKEN(HttpStatus.FORBIDDEN,"403" ,"refresh token이 유효하지 않습니다." ),
    NullPoint_Token(HttpStatus.METHOD_NOT_ALLOWED,"405", "사용자 정보가 만료되었습니다. 다시 로그인해 주세요"),

    NOT_EXIST_REFRESHTOKEN(HttpStatus.BAD_REQUEST, "400","올바른 RefreshToken을 헤더에 넣어주세요" ),
    TOO_HIGH_NUMBER(HttpStatus.BAD_REQUEST,"400" ,"금액은 8자리까지 입력할 수있습니다."),
    TOO_LONG_COMMENT(HttpStatus.BAD_REQUEST,"400" ,"댓글은 80자까지 입력할 수있습니다."),
    NOT_EXIST_COMMENT(HttpStatus.BAD_REQUEST,"400" , "존재하지 않는 댓글입니다. 새로고침 해주세요."),
    NOT_EXIST_CLIENT(HttpStatus.BAD_REQUEST,"400", "탈퇴처리가 완료되었습니다. 새로고침 해주세요.");

    // 추후 추가 코드
    private boolean success;
    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String msg;

}
