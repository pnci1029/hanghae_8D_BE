package com.example.checkcheck.repository;

import com.example.checkcheck.dto.responseDto.ArticleResponseDto;
import com.example.checkcheck.dto.responseDto.MyPageResponseDto;
import com.example.checkcheck.model.articleModel.Category;
import com.example.checkcheck.model.articleModel.CategoryEntity;
import com.example.checkcheck.model.articleModel.Process;
import com.example.checkcheck.security.UserDetailsImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface ArticleRepositoryCustom {
    Slice<ArticleResponseDto> articleScroll(Pageable pageable, Category category, Process process);

    List<ArticleResponseDto> articleCarousel();

    List<MyPageResponseDto> myPageInfo(UserDetailsImpl userDetails, Process process);

}
