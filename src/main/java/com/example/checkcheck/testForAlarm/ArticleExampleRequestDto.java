package com.example.checkcheck.testForAlarm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "게시물 요청 객체", description = "게시물 요청을 보내기 위한 객체")
public class ArticleExampleRequestDto {

   /**
   * Article 알람용 예제입니다
    */
   @ApiModelProperty(value = "유저 이메일",example = "g_xxx@gmail.com", required = true)
    private String userEmail;

}
