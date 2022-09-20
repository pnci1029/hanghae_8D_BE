package com.example.checkcheck.security;

import com.example.checkcheck.dto.responseDto.ResponseDto;
import com.google.gson.JsonObject;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.NoSuchElementException;

@Component
public class JwtExceptionFilter extends GenericFilterBean {
    private final HttpServletResponse httpServletResponse;

    public JwtExceptionFilter(HttpServletResponse httpServletResponse) {
        this.httpServletResponse = httpServletResponse;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        try {
            chain.doFilter(request, response); // go to JwtAuthenticationFilter
        } catch (JwtException | NullPointerException e) {
            setErrorResponse(HttpStatus.BAD_REQUEST, response, e);
            httpServletResponse.setStatus(415);
        } catch (NoSuchElementException e) {
            setErrorResponse(HttpStatus.UNAUTHORIZED, response, e);
            httpServletResponse.setStatus(300);
        }
    }

    public void setErrorResponse(HttpStatus status, ServletResponse response, Throwable e) throws IOException {
        ((HttpServletResponse)response).setStatus(status.value());
        response.setContentType("application/json; charset=UTF-8");

        JwtExceptionResponse jwtExceptionResponse = new JwtExceptionResponse(false, e.getMessage(), status.value());
        response.getWriter().write(jwtExceptionResponse.convertToJson());


        ResponseDto.success("ㅎㅇ");
    }
}

