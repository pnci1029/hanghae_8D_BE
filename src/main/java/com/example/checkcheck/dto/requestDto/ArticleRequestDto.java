package com.example.checkcheck.dto.requestDto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class ArticleRequestDto {

    @NotNull(message = "제목을 입력해 주세요.")
    private String title;
    @NotNull(message = "품목에 대한 설명을 작성해 주세요.")
    private String content;
    @NotNull(message = "가격을 입력해 주세요.")
    private int price;
//    @NotNull(message = "카테고리를 선택해 주세요.")
    private String category;
    private List<String> imageList;



}
