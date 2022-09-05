package com.example.checkcheck.repository;


import com.example.checkcheck.model.articleModel.Article;
import com.example.checkcheck.model.commentModel.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByArticle(Article article);
}
