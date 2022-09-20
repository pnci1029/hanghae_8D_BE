package com.example.checkcheck.repository;

import com.example.checkcheck.model.Image;
import com.example.checkcheck.model.articleModel.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findByUserEmail(String userEmail);
    List<Image> findByArticle_ArticleId(Long articleId);

    void deleteByImage(String image);
}
