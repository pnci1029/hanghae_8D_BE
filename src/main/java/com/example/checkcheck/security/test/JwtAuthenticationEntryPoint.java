package com.example.checkcheck.security.test;

import com.example.checkcheck.exception.CustomException;
import com.example.checkcheck.exception.ErrorCode;
import org.aspectj.apache.bcel.classfile.Code;
import org.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        String exception = request.getAttribute("exception").toString();

        if(exception == null){
            setResponse(response, ErrorCode.FORBIDDEN_EXCEPTION);
        }
        else {
            System.out.println(66);
            setResponse(response, ErrorCode.FORBIDDEN_EXCEPTION);
        }
    }



    //401상태 에러 http json 타입으로 만들어 리턴
    public void setResponse(HttpServletResponse response, ErrorCode code) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // response에 한글 메세지를 담기위해 JSONObject 선언
        JSONObject responseJson = new JSONObject();
        responseJson.put("message", code.getMsg()); // 넘어온 code의 메세지 입력
        responseJson.put("errorCode", code.getErrorCode());
        if(code.getErrorCode().equals("401")) // 해당 코드에 맞춰 상태메세지 변환
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        else if(code.getErrorCode().equals("400"))
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        else if(code.getErrorCode().equals("408"))
            response.setStatus(HttpServletResponse.SC_REQUEST_TIMEOUT);
        else if(code.getErrorCode().equals("1006"))
            response.setStatus(HttpServletResponse.SC_OK);
        else if(code.getErrorCode().equals("1007"))
            response.setStatus(HttpServletResponse.SC_OK);
        // response의 header와 body담아 리턴
        response.getWriter().print(responseJson);
    }
}
