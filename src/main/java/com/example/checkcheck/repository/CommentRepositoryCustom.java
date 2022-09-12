package com.example.checkcheck.repository;

import com.example.checkcheck.model.commentModel.Comment;

import java.util.List;

public interface CommentRepositoryCustom {
    List<Comment> getCommentList(Long articleId);
}
