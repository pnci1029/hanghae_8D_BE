package com.example.checkcheck.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
/**
* exception 처리를 위해 임의로 만들어두었습니다.
* TODO: 임시방편으로 만들어두었기 때문에 나중에 우리 서비스에 맞춰서 변경 필요
*  추후 모든 Controller 형식을 ResponseEntity 형식으로 변경해주기만 하면 됩니다 *
*/

public enum ErrorCode {

    // 회원가입 + 로그인 + 이메일 체크
    DUPLE_EMAIL(HttpStatus.BAD_REQUEST, "400", "중복된 이메일 입니다."),
    EMAIL_CONTENT_END(HttpStatus.BAD_REQUEST, "400", ""),
    INCORRECT_EMAIL_CODE(HttpStatus.BAD_REQUEST, "400", "이메일 인증에 실패하였습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "사용자를 찾을 수 없습니다."),
    CONFIRM_EMAIL_PWD(HttpStatus.BAD_REQUEST, "400", "이메일 또는 비밀번호를 확인해주세요."),

    INVALID_OLD_PWD(HttpStatus.BAD_REQUEST, "400", "기존 비밀번호가 옳바르지 않습니다."),

    NOT_LOGIN(HttpStatus.BAD_REQUEST, "400", "로그인이 필요합니다."),
    UNKNOWN_ERROR(HttpStatus.UNAUTHORIZED,"403", "토큰이 존재하지 않습니다1."),
    NullPoint_Token(HttpStatus.UNAUTHORIZED,"403", "토큰이 존재하지 않습니다2."),

    NOT_EXPIRED_TOKEN_YET(HttpStatus.BAD_REQUEST,"400", "토큰이 만료되지 않았습니다."),
    EXPIRED_TOKEN(HttpStatus.BAD_REQUEST,"401", "만료된 토큰입니다."),
    ACCESS_DENIED(HttpStatus.BAD_REQUEST,"401", "유효한 토큰이 아닙니다."),

    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST,"400","유효하지 않은 리프레시 토큰입니다."),

    NEED_EMAIL(HttpStatus.BAD_REQUEST,"400","이메일은 필수로 동의 해주셔야 합니다."),

    FILE_NULL(HttpStatus.BAD_REQUEST, "400", "파일을 입력해주세요."),
    UnSupported_Token(HttpStatus.UNAUTHORIZED, "401", "올바른 JWT 토큰을 입력해주세요"),
    Signature_Exception(HttpStatus.BAD_REQUEST, "404", "변조된 JWT 토큰입니다"),

    // 알림 URL, MESSAGE
    NOT_VALIDURL(HttpStatus.BAD_REQUEST,"400","요효하지 않는 URL 입니다."),
    NOT_VALIDMESSAGE(HttpStatus.BAD_REQUEST,"400","유효하지 않는 내용입니다."),
    NOT_EXIST_NOTIFICATION(HttpStatus.NOT_FOUND,"404","존재하지 않는 알림입니다."),

    // 게시글
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "해당 게시글이 존재하지 않습니다"),
    CHALLENGE_NOT_UPDATE(HttpStatus.BAD_REQUEST, "400", "첼린지는 수정 불가 합니다."),
    CHALLENGE_CANCEL_APPLY_NOT(HttpStatus.BAD_REQUEST, "400", "해당 챌린지는 마감되어 신청/취소할 수 없습니다."),
    CHALLENGE_CANCEL_AUTHOR_NOT(HttpStatus.BAD_REQUEST, "400", "첼린지 게시물을 작성한 사용자는 신청/취소할 수 없습니다."),

    //댓글



    // 정렬 옵션,
    INVALID_SORTING_OPTION(HttpStatus.BAD_REQUEST, "400", "잘못된 sort 옵션입니다"),
    INVALID_FILTER_OPTION(HttpStatus.BAD_REQUEST, "400", "잘못된 filter 옵션입니다"),
    SUBSCRIBE_ERROR(HttpStatus.BAD_REQUEST, "400", "잘못된 Subscribe 요청입니다"),

    ;

    // 추후 추가 코드
    private boolean success;
    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String msg;

}
