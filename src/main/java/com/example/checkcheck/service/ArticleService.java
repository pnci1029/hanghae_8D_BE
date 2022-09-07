package com.example.checkcheck.service;

import com.example.checkcheck.dto.requestDto.ArticleRequestDto;
import com.example.checkcheck.dto.responseDto.ArticleDetailResponseDto;
import com.example.checkcheck.dto.responseDto.ArticleResponseDto;
import com.example.checkcheck.dto.responseDto.ResponseDto;
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
import com.example.checkcheck.util.LoadUser;
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
    private LoadUser loadUser;

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
    public ResponseDto<?> postArticles(List<MultipartFile> multipartFile, ArticleRequestDto articleRequestDto,
                                       UserDetailsImpl userDetails) throws IOException {

            String nickName = userDetails.getMember().getNickName();
            String userEmail = userDetails.getUsername();

        //        유저 포인트
            Optional<Member> memberBox = memberRepository.findByUserEmail(userEmail);
            int userPoint = userDetails.getMember().getPoint() + 2;
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
                    .member(userDetails.getMember())
                    .userEmail(userEmail)
                    .build();
            articleRepository.save(articles);

//        작성시간
            String time1 = comfortUtils.getTime(articles.getCreatedAt());

////        이미지업로드
            if (multipartFile != null) {

                List<Image> imgbox = new ArrayList<>();
                //          이미지 업로드
//            for (MultipartFile uploadedFile : multipartFile) {
//
//                Image imagePostEntity = Image.builder()
//                        .image(s3Uploader.upload(uploadedFile))
//                        .userEmail(userEmail)
//                        .article(articles)
//                        .build();
//                imgbox.add(imagePostEntity);
//
//                imageRepository.save(imagePostEntity);
//            }

            }

            return ResponseDto.success("작성 성공");

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

//  게시글 상세페이지
    public ArticleDetailResponseDto getArticleDetail(Long id, UserDetailsImpl userDetails) {
        Article article = articleRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지않습니다")
        );
        Boolean isMyArticles = false;
        if (article.getMember().getMemberId().equals(userDetails.getMember().getMemberId())) {
            isMyArticles = true;
        }

        List<Image> articleList = imageRepository.findByArticle_ArticleId(id);

        List<String> imageBox = new ArrayList<>();
        for (Image image : articleList) {
            String realImage = image.getImage();
            imageBox.add(realImage);
        }

        String category = comfortUtils.getCategoryKorean(article.getCategory());

        ArticleDetailResponseDto articleResponseDto = ArticleDetailResponseDto.builder()
                .article(article)
                .isMyArticles(isMyArticles)
                .image(imageBox)
                .category(category)
                .build();


        return articleResponseDto;
    }

    @Transactional
    public ResponseDto<?> deleteArticle(Long articlesId, UserDetailsImpl userDetails) {
        Optional<Article> target = articleRepository.findById(articlesId);
        System.out.println("userDetails = " + userDetails.getUsername());
        System.out.println("target.get().getMember().getUserEmail() = " + target.get().getMember().getUserEmail());

        if (!userDetails.getUsername().equals(target.get().getMember().getUserEmail())) {
            return ResponseDto.fail("400", "게시글 작성자만 삭제가 가능합니다.");
        }
        articleRepository.deleteById(target.get().getArticleId());
        return ResponseDto.success("삭제 성공");
    }




































    // 게시글 번호 확인
    @Transactional
    public Article isPresentArticle(Long id) {
        Optional<Article> optionalArticle = articleRepository.findById(id);
        return optionalArticle.orElse(null);
    }



}