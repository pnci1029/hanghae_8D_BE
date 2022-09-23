package com.example.checkcheck.dto.requestDto;

import com.example.checkcheck.model.articleModel.Category;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.bytedeco.javacpp.presets.opencv_core;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
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

//    @JsonCreator
//    public ArticleRequestDto(
//            @JsonProperty("title") String title,
//            @JsonProperty("content") String content,
//            @JsonProperty("price") int price,
//            @JsonProperty("category") String category
//    ) {
//
//        this.title = title(title);
//        this.content = content(content);
//        this.price = price(price);
//        this.category = Category.valueOf(category(Category.valueOf(category)));
//    }
//
//    private String title(String title) {
//        return String.valueOf(title);
//    }
//
//    private String content(String content) {
//        return String.valueOf(content);
//    }
//    private int price(int price) {
//        return price;
//    }
//
//    private String category(Category category) {
//        return String.valueOf(category);
////        return Arrays.stream(Category.values())
////                .filter(type -> StringUtils.startsWithIgnoreCase(type.name().toLowerCase(), category))
////                .findFirst().orElseGet(() -> null);
//    }

}
