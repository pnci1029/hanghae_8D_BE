package com.example.checkcheck.service;

import com.example.checkcheck.dto.requestDto.ArticleRequestDto;
import com.example.checkcheck.dto.responseDto.ArticleResponseDto;
import com.example.checkcheck.model.Article;
import com.example.checkcheck.model.Image;
import com.example.checkcheck.model.Member;
import com.example.checkcheck.repository.ArticleRepository;
import com.example.checkcheck.repository.ImageRepository;
import com.example.checkcheck.repository.UserRepository;
import com.example.checkcheck.service.notification.NotificationService;
import com.example.checkcheck.service.s3.S3Uploader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {

    private ArticleRepository articleRepository;
    private ImageRepository imageRepository;
    private UserRepository userRepository;
    private S3Uploader s3Uploader;

    /**
     * 알람 기능 의존성 주입
     */
    private NotificationService notificationService;

    public ArticleService(ArticleRepository articleRepository, S3Uploader s3Uploader,
                          ImageRepository imageRepository, UserRepository userRepository,
                          NotificationService notificationService) {
        this.articleRepository = articleRepository;
        this.s3Uploader = s3Uploader;
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }



    public ArticleResponseDto postArticles(List<MultipartFile> multipartFile, ArticleRequestDto articleRequestDto) throws IOException {

        String nickname = "k_nadazerg1@naver.com";

//        게시글
        Article articles = Article.builder()
                .nickName(nickname)
                .title(articleRequestDto.getTitle())
                .content(articleRequestDto.getContent())
                .price(articleRequestDto.getPrice())
                .category(String.valueOf(articleRequestDto.getCategory()))
                .build();
        articleRepository.save(articles);

//        유저 포인트
        Optional<Member> users = userRepository.findByUserEmail(nickname);

        if (multipartFile != null) {


            List<Image> imgbox = new ArrayList<>();
            //          이미지 업로드
            for (MultipartFile uploadedFile : multipartFile) {

                Image imagePostEntity = Image.builder()
                        .image(s3Uploader.upload(uploadedFile))
                        .userEmail(nickname)
                        .article(articles)
                        .build();
                imgbox.add(imagePostEntity);

                imageRepository.save(imagePostEntity);
            }

        } else {

        }
        return null;
    }
}
