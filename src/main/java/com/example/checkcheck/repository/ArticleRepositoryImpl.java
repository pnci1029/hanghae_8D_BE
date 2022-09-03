package com.example.checkcheck.repository;

import com.example.checkcheck.dto.responseDto.ArticleResponseDto;
import com.example.checkcheck.model.Image;
import com.example.checkcheck.model.articleModel.Article;
import com.example.checkcheck.model.articleModel.Category;
import com.example.checkcheck.model.articleModel.Process;
import com.example.checkcheck.model.articleModel.QArticle;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.ArrayList;
import java.util.List;

import static com.example.checkcheck.model.articleModel.QArticle.*;

public class ArticleRepositoryImpl implements ArticleRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    public ArticleRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }


    public Slice<ArticleResponseDto> articleScroll(Pageable pageable, Category category, Process process) {
        QueryResults<Article> articleQueryResults = jpaQueryFactory
                .selectFrom(article)
                .where(article.category.eq(category).and(article.process.eq(process)))
                .offset(pageable.getOffset())
                .limit(pageable.getOffset() + 1)
                .orderBy(article.createdAt.desc())
                .fetchResults();

        List<ArticleResponseDto> content = new ArrayList<>();
        for (Article article : articleQueryResults.getResults()) {
//            List<String> imageBox = new ArrayList<>();

//            List<Image> imageList = imageRepository.findByArticle_ArticleId(article.getArticleId());
//            for (Image image : imageList) {
//                imageBox.add(image.getImage());
//            }
            ArticleResponseDto articleResponseDto = ArticleResponseDto.builder()
                    .article(article)
//                    .image(imageBox)
                    .build();
            content.add(articleResponseDto);
        }
        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext = true;
        }
        return new SliceImpl<>(content, pageable, hasNext);

    }
}
