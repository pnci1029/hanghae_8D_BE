package com.example.checkcheck.controller;

import com.example.checkcheck.dto.requestDto.ArticleRequestDto;
import com.example.checkcheck.security.UserDetailsImpl;
import com.example.checkcheck.service.ArticleService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequestMapping("/api/auth")
@RestController
public class ArticleController {

    private ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping(value = "/form", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Object> upload(@RequestPart(required = false) ArticleRequestDto articlesDto,
                                                    @RequestPart (required = false) List<MultipartFile> multipartFile,
                                                    @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return articleService.postArticles(multipartFile, articlesDto, userDetails);
    }
}
