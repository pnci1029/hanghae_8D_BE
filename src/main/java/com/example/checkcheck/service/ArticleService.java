package com.example.checkcheck.service;

import com.example.checkcheck.dto.requestDto.ArticleRequestDto;
import com.example.checkcheck.dto.responseDto.ArticleResponseDto;
import com.example.checkcheck.model.Image;
import com.example.checkcheck.model.articleModel.Article;
import com.example.checkcheck.model.Member;
import com.example.checkcheck.model.articleModel.Category;
import com.example.checkcheck.model.articleModel.Process;
import com.example.checkcheck.repository.ArticleRepository;
import com.example.checkcheck.repository.ImageRepository;
import com.example.checkcheck.repository.MemberRepository;
import com.example.checkcheck.security.UserDetailsImpl;
import com.example.checkcheck.service.notification.NotificationService;
import com.example.checkcheck.service.s3.S3Uploader;
import com.example.checkcheck.util.ComfortUtils;
import com.example.checkcheck.util.Time;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {

    private ArticleRepository articleRepository;
    private ImageRepository imageRepository;
    private MemberRepository memberRepository;
    private S3Uploader s3Uploader;
    private ComfortUtils comfortUtils;
    private Time time;

    private NotificationService notificationService;

    public ArticleService(
            ArticleRepository articleRepository,
            ImageRepository imageRepository,
            MemberRepository memberRepository,
            S3Uploader s3Uploader,
            ComfortUtils comfortUtils,
            Time time,
            NotificationService notificationService) {
        this.articleRepository = articleRepository;
        this.imageRepository = imageRepository;
        this.memberRepository = memberRepository;
        this.s3Uploader = s3Uploader;
        this.comfortUtils = comfortUtils;
        this.time = time;
        this.notificationService = notificationService;
    }

    @Transactional
    public ResponseEntity<Object> postArticles(List<MultipartFile> multipartFile, ArticleRequestDto articleRequestDto,
                                               UserDetailsImpl userDetails) throws IOException {

        String nickName = userDetails.getMember().getNickName();
        String userEmail = userDetails.getUsername();


        //        유저 포인트
        Optional<Member> memberBox = memberRepository.findByUserEmail(userEmail);
        int userPoint= userDetails.getMember().getPoint()+2;
        memberBox.get().updatePoint(userPoint);

        String userRank = comfortUtils.getUserRank(memberBox.get().getPoint());

        //        게시글
        Article articles = Article.builder()
                .nickName(nickName)
                .title(articleRequestDto.getTitle())
                .content(articleRequestDto.getContent())
                .price(articleRequestDto.getPrice())
                .category(articleRequestDto.getCategory())
                .process(Process.process)
                .userRank(userRank)
                .build();
        articleRepository.save(articles);

//        작성시간
        String time1 = comfortUtils.getTime(articles.getCreatedAt());

////        이미지업로드
        if (multipartFile != null) {

            List<Image> imgbox = new ArrayList<>();
            //          이미지 업로드
            for (MultipartFile uploadedFile : multipartFile) {

                Image imagePostEntity = Image.builder()
                        .image(s3Uploader.upload(uploadedFile))
                        .userEmail(userEmail)
                        .article(articles)
                        .build();
                imgbox.add(imagePostEntity);

                imageRepository.save(imagePostEntity);
            }

        }

        return ResponseEntity.ok().build();
    }

    public List<ArticleResponseDto> getArticleCarousel() {
        List<ArticleResponseDto> articleResult = articleRepository.articleCarousel();
        Collections.shuffle(articleResult);

        List<ArticleResponseDto> resultBox = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            resultBox.add(articleResult.get(i));
        }
        return resultBox;
    }









































    // 게시글 번호 확인
    @Transactional
    public Article isPresentArticle(Long id) {
        Optional<Article> optionalArticle = articleRepository.findById(id);
        return optionalArticle.orElse(null);
    }
}