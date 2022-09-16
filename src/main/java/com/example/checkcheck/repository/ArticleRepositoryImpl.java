package com.example.checkcheck.repository;

import com.example.checkcheck.dto.responseDto.ArticleResponseDto;
import com.example.checkcheck.dto.responseDto.MyPageResponseDto;
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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

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

        if (category == null) {
            category = Category.all;
        }
        if (process == null) {
            process = Process.all;
        }
//                전체 불러오기
        if (process.equals(Process.all) && category.equals(Category.all)) {
            articleQueryResults = jpaQueryFactory
                    .selectFrom(article)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize() + 1)
                    .orderBy(article.articleId.desc())
                    .fetchResults();

            //            프로세스 전체
        } else if (process.equals(Process.all)) {
            articleQueryResults = jpaQueryFactory
                    .selectFrom(article)
                    .where(article.category.eq(category))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize() + 1)
                    .orderBy(article.articleId.desc())
                    .fetchResults();

            //            카테고리 전체
        } else if (category.equals(Category.all)) {
            articleQueryResults = jpaQueryFactory
                    .selectFrom(article)
                    .where(article.process.eq(process))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize() + 1)
                    .orderBy(article.articleId.desc())
                    .fetchResults();
        }
//                    선택된값 조회
        else {
            articleQueryResults = jpaQueryFactory
                    .selectFrom(article)
                    .where(article.process.eq(process).and(article.category.eq(category)))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize() + 1)
                    .orderBy(article.articleId.desc())
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

//                진행상태가 진행중일때
            if (article.getProcess().equals(Process.process)) {
                ArticleResponseDto articleResponseDto = ArticleResponseDto.builder()
                        .article(article)
                        .userRank(comfortUtils.getUserRank(article.getMember().getPoint()))
                        .image(images)
                        .process(comfortUtils.getProcessKorean(article.getProcess()))
//                            게시자가 올린 금액 OK 선택 금액 NUll
                        .price(NumberFormat.getInstance().format(article.getPrice()))
                        .selectedPrice(null)
                        .build();
                content.add(articleResponseDto);
//                    완료가 된 경우
            } else {
                ArticleResponseDto articleResponseDto = ArticleResponseDto.builder()
                        .article(article)
                        .userRank(comfortUtils.getUserRank(article.getMember().getPoint()))
                        .image(images)
                        .process(comfortUtils.getProcessKorean(article.getProcess()))
                        .price(null)
                        .selectedPrice(NumberFormat.getInstance().format(article.getSelectedPrice()))
                        .build();
                content.add(articleResponseDto);
            }

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
                    .price(NumberFormat.getInstance().format(article.getPrice()))
                    .userRank(comfortUtils.getUserRank(article.getMember().getPoint()))
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
                    .orderBy(article.articleId.desc())
                    .fetch();
        } else {
            result = jpaQueryFactory
                    .selectFrom(article)
                    .where(article.member.eq(userDetails.getMember()).and(article.process.eq(process)))
                    .orderBy(article.articleId.desc())
                    .fetch();
        }
        for (Article articles : result) {

//        썸네일 이미지만 저장
            String image = "";
            List<Image> imageList = imageRepository.findByArticle_ArticleId(articles.getArticleId());
            for (Image images : imageList) {
                image = images.getImage();
                break;
            }

            int point = 0;
//            게시글 상태에 따라 다른 포인트로 리스폰스
            if (articles.getProcess().equals(Process.done)) {
                point = 12;
            } else {
                point = 2;
            }

            String dotNum = NumberFormat.getInstance().format(articles.getPrice());
            String SelDotNum = NumberFormat.getInstance().format(articles.getSelectedPrice());
//            진행 중일 때 리스폰스
            if (articles.getProcess().equals(Process.process)) {
                MyPageResponseDto myPageResponseDto = MyPageResponseDto.builder()
                        .articlesId(articles.getArticleId())
                        .title(articles.getTitle())
//                        프론트 이슈
                        .process(comfortUtils.getProcessKorean(articles.getProcess()))
                        .price(dotNum)
                        .selectedPrice(null)
                        .image(image)
                        .point(point)
                        .build();
                resultList.add(myPageResponseDto);
                
//                완료 상태일 때 리스폰스
            } else {
                MyPageResponseDto myPageResponseDto = MyPageResponseDto.builder()
                        .articlesId(articles.getArticleId())
                        .title(articles.getTitle())
//                        프론트 이슈
                        .process(comfortUtils.getProcessKorean(articles.getProcess()))
                        .price(null)
                        .selectedPrice(SelDotNum)
                        .image(image)
                        .point(point)
                        .build();
                resultList.add(myPageResponseDto);
            }

        }

        return resultList;
    }

}
