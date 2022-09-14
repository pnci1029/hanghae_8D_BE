package com.example.checkcheck.dto.requestDto;

import com.example.checkcheck.model.articleModel.Category;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ArticleRequestDto {

    private String title;
    private String content;
    private int price;
    private Category category;
    private List<String> imageList;

}
