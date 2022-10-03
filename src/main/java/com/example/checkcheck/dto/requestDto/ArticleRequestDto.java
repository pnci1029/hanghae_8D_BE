package com.example.checkcheck.dto.requestDto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class ArticleRequestDto {

    @NotNull(message = "제목을 입력해 주세요.")
    @Size(min = 1, max = 30, message = "제목은 1~30글자로 작성해주세요.")
    private String title;
    @NotNull(message = "품목에 대한 설명을 작성해 주세요.")
    @Size(min = 1, max = 400, message = "내용은 1~400글자로 작성해주세요.")
    private String content;
    @NotNull(message = "가격을 입력해 주세요.")
    @Length(max = 8, message = "금액은 8자리까지 작성 가능합니다.")
    private int price;
    private String category;
    private List<String> imageList;



}
