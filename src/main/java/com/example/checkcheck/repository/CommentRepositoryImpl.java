package com.example.checkcheck.repository;

import com.example.checkcheck.model.commentModel.Comment;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;

import static com.example.checkcheck.model.commentModel.QComment.comment1;

public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public CommentRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }


    //    댓글 조회
    public List<Comment> getCommentList(Long id) {

        List<Comment> result = jpaQueryFactory
                .selectFrom(comment1)
                .where(comment1.article.articleId.eq(id))
                .orderBy(comment1.isSelected.desc())
                .orderBy(comment1.commentId.asc())
                .fetch();


        return result;

    }
}
