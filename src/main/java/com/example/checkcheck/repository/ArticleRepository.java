package com.example.checkcheck.repository;

import com.example.checkcheck.model.Member;
import com.example.checkcheck.model.articleModel.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long>, ArticleRepositoryCustom {

    List<Article> findAllByMember(Member member);


    List<Article> findByUserEmail(String userEmail);
}
