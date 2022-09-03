package com.example.checkcheck.repository;

import com.example.checkcheck.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {

}
