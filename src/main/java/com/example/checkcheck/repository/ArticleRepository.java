package com.example.checkcheck.repository;

import com.example.checkcheck.model.articleModel.Article;
import com.example.checkcheck.model.articleModel.Category;
import com.example.checkcheck.model.articleModel.Process;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long>, ArticleRepositoryCustom {

    List<Article> findByCategoryAndProcess(Category category, Process process);

    List<Article> findByCategory(Category category);

}
