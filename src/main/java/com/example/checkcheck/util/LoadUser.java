package com.example.checkcheck.util;

import com.example.checkcheck.exception.CustomException;
import com.example.checkcheck.exception.ErrorCode;
import com.example.checkcheck.model.Member;
import com.example.checkcheck.security.UserDetailsImpl;
import org.springframework.security.core.context.SecurityContextHolder;

/**
* Member Entity Validation 관련 부분 아직 제가 정리가 안되서 임시로 만들어두었습니다.
*/
public class LoadUser {
    public static String getEmail(){
//        Member principal = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("username = " + username);
        return username;
    }


    public static void loginCheck(){
        Member principal = (Member)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal == null){
            throw new CustomException(ErrorCode.NOT_LOGIN);
        }
    }

    public static void loginAndEmailCheck(){
        Member principal = (Member)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal == null){
            throw new CustomException(ErrorCode.NOT_LOGIN);
        }
        if (principal.getPassword().isEmpty()){
            throw new CustomException(ErrorCode.CONFIRM_EMAIL_PWD);
        }
    }
}
