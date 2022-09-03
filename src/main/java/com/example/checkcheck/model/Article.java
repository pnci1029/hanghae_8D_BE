package com.example.checkcheck.model;

import com.example.checkcheck.dto.requestDto.ArticleRequestDto;
import com.example.checkcheck.dto.responseDto.ArticleResponseDto;
import com.example.checkcheck.util.TimeStamped;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Article extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articlesId;

    @Column(nullable = false)
    private String nickName;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int selectedPrice;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Image> images;

//    이거 왜 숫자로 나오냐,,,?
    @Column
    private Category category;

    @Builder
    public Article(String nickName, String title, String content, Category category,
                   int price, int selectedPrice, List<Image> images) {
//        this.articlesId = articlesId;
        this.nickName = nickName;
        this.title = title;
        this.category = category;
        this.content = content;
        this.price = price;
        this.selectedPrice = selectedPrice;
        this.images = images;
    }
}
