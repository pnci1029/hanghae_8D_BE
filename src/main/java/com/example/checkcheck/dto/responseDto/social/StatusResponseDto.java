package com.example.checkcheck.dto.responseDto.social;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StatusResponseDto {
    private  String msg;
    private Object data;
}
