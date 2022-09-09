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
    private String price;
    private String image;
    private String process;
    private int point;

}
