package com.example.checkcheck.controller;

import com.example.checkcheck.dto.requestDto.SearchRequestDto;
import com.example.checkcheck.dto.responseDto.ArticleResponseDto;
import com.example.checkcheck.repository.ArticleRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SearchController {
    private ArticleRepository articleRepository;

    public SearchController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/search")
    public List<ArticleResponseDto> searchArticle(@RequestBody SearchRequestDto searchRequestDto) {
        return articleRepository.searchArticles(searchRequestDto);
    }
}
