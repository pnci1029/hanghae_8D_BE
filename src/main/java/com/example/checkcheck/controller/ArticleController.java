package com.example.checkcheck.controller;

import com.example.checkcheck.dto.requestDto.ArticleRequestDto;
import com.example.checkcheck.dto.responseDto.ArticleDetailResponseDto;
import com.example.checkcheck.dto.responseDto.ArticleResponseDto;
import com.example.checkcheck.dto.responseDto.ResponseDto;
import com.example.checkcheck.model.articleModel.Process;
import com.example.checkcheck.security.UserDetailsImpl;
import com.example.checkcheck.service.ArticleService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RequestMapping("/api")
@RestController
public class ArticleController {

    private ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping(value = "/auth/form", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseDto<?> upload(@RequestPart(required = false) @Valid ArticleRequestDto articlesDto,
                                 @RequestPart(required = false) List<MultipartFile> multipartFile,
                                 @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return articleService.postArticles(multipartFile, articlesDto, userDetails);
    }

    @GetMapping("/main/list")
    public Slice<ArticleResponseDto> getAllArticle(@RequestParam(value = "category", required = false) String category,
                                                   @RequestParam(value = "process", required = false) Process process,
                                                   Pageable pageable) {

        return articleService.getAllArticles(pageable, category, process);
    }

    @GetMapping("/main/randomcards")
    public ResponseDto<?> getArticleCarousel() {
        return articleService.getArticleCarousel();
    }

    @GetMapping("/auth/detail/{articlesId}")
    public ArticleDetailResponseDto getArticleDetail(@PathVariable Long articlesId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return articleService.getArticleDetail(articlesId, userDetails);
    }

    @PatchMapping("/auth/detail/{articlesId}")
    public ResponseDto<?> patchArticle(@RequestPart(required = false)@Valid ArticleRequestDto articlesDto,
                                     @RequestPart(required = false) List<MultipartFile> multipartFile,
                                     @PathVariable Long articlesId,
                                     @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return articleService.patchArticle(multipartFile, articlesDto, articlesId, userDetails);
    }


    @DeleteMapping("/auth/detail/{articlesId}")
    public ResponseDto<?> deleteArticle(@PathVariable Long articlesId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
       return  articleService.deleteArticle(articlesId, userDetails);
    }

}
