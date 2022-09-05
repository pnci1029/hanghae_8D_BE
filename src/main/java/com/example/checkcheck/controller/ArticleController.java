package com.example.checkcheck.controller;

import com.example.checkcheck.dto.requestDto.ArticleRequestDto;
import com.example.checkcheck.dto.responseDto.ArticleResponseDto;
import com.example.checkcheck.model.articleModel.Category;
import com.example.checkcheck.model.articleModel.Process;
import com.example.checkcheck.repository.ArticleRepository;
import com.example.checkcheck.security.UserDetailsImpl;
import com.example.checkcheck.service.ArticleService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequestMapping("/api")
@RestController
public class ArticleController {

    private ArticleService articleService;
    private ArticleRepository articleRepository;

    public ArticleController(ArticleService articleService, ArticleRepository articleRepository) {
        this.articleService = articleService;
        this.articleRepository = articleRepository;
    }

    @PostMapping(value = "/auth/form", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Object> upload(@RequestPart(required = false) ArticleRequestDto articlesDto,
                                         @RequestPart(required = false) List<MultipartFile> multipartFile,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return articleService.postArticles(multipartFile, articlesDto, userDetails);
    }

    @GetMapping("/main/list")
    public Slice<ArticleResponseDto> getAllArticle(@RequestParam(value = "category") Category category,
                                                   @RequestParam(value = "process") Process process,
                                                    Pageable pageable
//                                                   @RequestParam(value = "size") int size,
//                                                   @RequestParam(value = "page") int page
    ) {
//        page = page - 1;
//        return articleService.showAllArticle(category, process, size, page);
        return articleRepository.articleScroll(pageable, category, process);
    }

    @GetMapping("/main/randomcards")
    public List<ArticleResponseDto> getArticleCarousel() {
        return articleService.getArticleCarousel();
    }
}
