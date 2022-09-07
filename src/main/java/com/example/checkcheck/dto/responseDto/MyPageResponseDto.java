package com.example.checkcheck.dto.responseDto;

import com.example.checkcheck.model.Image;
import com.example.checkcheck.model.articleModel.Process;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MyPageResponseDto {
    private Long articlesId;
    private String title;
    private int price;
    private String image;
    private Process process;
    private int point;

}
