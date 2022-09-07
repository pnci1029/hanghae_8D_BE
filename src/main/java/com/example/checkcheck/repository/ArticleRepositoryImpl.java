package com.example.checkcheck.repository;

import com.example.checkcheck.dto.responseDto.ArticleResponseDto;
import com.example.checkcheck.dto.responseDto.MyPageResponseDto;
import com.example.checkcheck.dto.responseDto.ResponseDto;
import com.example.checkcheck.model.Image;
import com.example.checkcheck.model.articleModel.Article;
import com.example.checkcheck.model.articleModel.Category;
import com.example.checkcheck.model.articleModel.Process;
import com.example.checkcheck.security.UserDetailsImpl;
import com.example.checkcheck.util.ComfortUtils;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.ArrayList;
import java.util.List;

import static com.example.checkcheck.model.articleModel.QArticle.*;
import static com.example.checkcheck.model.articleModel.QArticle.article;

public class ArticleRepositoryImpl implements ArticleRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final ImageRepository imageRepository;
    private final ComfortUtils comfortUtils;

    public ArticleRepositoryImpl(JPAQueryFactory jpaQueryFactory, ImageRepository imageRepository, ComfortUtils comfortUtils) {
        this.jpaQueryFactory = jpaQueryFactory;
        this.imageRepository = imageRepository;
        this.comfortUtils = comfortUtils;
    }

    public Slice<ArticleResponseDto> articleScroll(Pageable pageable, Category category, Process process) {
//        전체조회
        QueryResults<Article> articleQueryResults = null;

        //        전체 불러오기
        if (process.equals(Process.all) && category.equals(Category.all)) {
            articleQueryResults = jpaQueryFactory
                    .selectFrom(article)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize() + 1)
                    .orderBy(article.createdAt.desc())
                    .fetchResults();

            //            카테고리 전체
        } else if (category.equals(Category.all)) {
            articleQueryResults = jpaQueryFactory
                    .selectFrom(article)
                    .where(article.process.eq(process))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize() + 1)
                    .orderBy(article.createdAt.desc())
                    .fetchResults();
        }

        //            프로세스 전체
        else if (process.equals(Process.all)) {
            articleQueryResults = jpaQueryFactory
                .selectFrom(article)
                .where(article.category.eq(category))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .orderBy(article.createdAt.desc())
                .fetchResults();
       }

        //            선택된값 조회
        else {
            articleQueryResults = jpaQueryFactory
                    .selectFrom(article)
                    .where(article.category.eq(category).and(article.process.eq(process)))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize() + 1)
                    .orderBy(article.createdAt.desc())
                    .fetchResults();
        }


        List<ArticleResponseDto> content = new ArrayList<>();
            for (Article article : articleQueryResults.getResults()) {
                String images = null;
                List<Image> imageList = imageRepository.findByArticle_ArticleId(article.getArticleId());
                for (Image image : imageList) {
                    images = image.getImage();
                    break;
                }
                String userRank = comfortUtils.getUserRank(article.getMember().getPoint());
                ArticleResponseDto articleResponseDto = ArticleResponseDto.builder()
                        .article(article)
                        .userRank(userRank)
                        .image(images)
                        .build();
                content.add(articleResponseDto);
            }



            boolean hasNext = false;
            if (content.size() > pageable.getPageSize()) {
                content.remove(pageable.getPageSize());
                hasNext = true;
            }
            return new SliceImpl<>(content, pageable, hasNext);

//
        }


    public List<ArticleResponseDto> articleCarousel() {
        List<ArticleResponseDto> articleResult = new ArrayList<>();

        List<Article> result = jpaQueryFactory
                .selectFrom(article)
                .where(article.process.eq(Process.process))
                .fetch();
        for (Article article : result) {
            String images = "";
            List<Image> imageList = imageRepository.findByArticle_ArticleId(article.getArticleId());
            for (Image image : imageList) {
                images = image.getImage();
                break;
            }
            ArticleResponseDto articleResponseDto = ArticleResponseDto.builder()
                    .article(article)
                    .image(images)
                    .build();
            articleResult.add(articleResponseDto);
        }
        return articleResult;
    }

//    마이페이지 게시글 조회
    public List<MyPageResponseDto> myPageInfo(UserDetailsImpl userDetails, Process process) {
        List<MyPageResponseDto> resultList = new ArrayList<>();


            List<Article> result = new ArrayList<>();
            if (process.equals(Process.all)) {
                result = jpaQueryFactory
                        .selectFrom(article)
                        .where(article.member.eq(userDetails.getMember()))
                        .fetch();
            } else {
                result = jpaQueryFactory
                        .selectFrom(article)
                        .where(article.member.eq(userDetails.getMember()).and(article.process.eq(process)))
                        .fetch();
            }

//        썸네일 이미지만 저장
            String image = "";
            List<Image> imageList = imageRepository.findByUserEmail(userDetails.getUsername());
            for (Image images : imageList) {
                image = images.getImage();
                break;
            }

            for (Article articles : result) {
                MyPageResponseDto myPageResponseDto = MyPageResponseDto.builder()
                        .articlesId(articles.getArticleId())
                        .title(articles.getTitle())
                        .process(articles.getProcess())
                        .price(articles.getPrice())
                        .image(image)
                        .point(articles.getMember().getPoint())
                        .build();
                resultList.add(myPageResponseDto);
            }

        return resultList;
    }

}
