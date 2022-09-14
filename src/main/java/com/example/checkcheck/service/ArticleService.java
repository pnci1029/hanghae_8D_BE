package com.example.checkcheck.service;

import com.example.checkcheck.dto.requestDto.ArticleRequestDto;
import com.example.checkcheck.dto.responseDto.ArticleDetailResponseDto;
import com.example.checkcheck.dto.responseDto.ArticleResponseDto;
import com.example.checkcheck.dto.responseDto.ResponseDto;
import com.example.checkcheck.dto.responseDto.TokenFactory;
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
//import com.example.checkcheck.service.s3.S3Uploader;
import com.example.checkcheck.service.s3.MarvinS3Uploader;
import com.example.checkcheck.util.ComfortUtils;
import com.example.checkcheck.util.LoadUser;
import com.example.checkcheck.util.Time;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
//    private S3Uploader s3Uploader;
    private MarvinS3Uploader marvinS3Uploader;
    private ComfortUtils comfortUtils;
    private Time time;


    private NotificationService notificationService;
    private LoadUser loadUser;

    public ArticleService(
            ArticleRepository articleRepository,
            ImageRepository imageRepository,
            MemberRepository memberRepository,
//            S3Uploader s3Uploader,
            MarvinS3Uploader marvinS3Uploader,
            ComfortUtils comfortUtils,
            Time time,
            NotificationService notificationService
            )
    {
        this.articleRepository = articleRepository;
        this.imageRepository = imageRepository;
        this.memberRepository = memberRepository;
//        this.s3Uploader = s3Uploader;
        this.marvinS3Uploader = marvinS3Uploader;
        this.comfortUtils = comfortUtils;
        this.time = time;
        this.notificationService = notificationService;
    }

    @Transactional
    public ResponseDto<?> postArticles(List<MultipartFile> multipartFile, ArticleRequestDto articleRequestDto,
                                       UserDetailsImpl userDetails) throws IOException {
//        try {
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
                    .selectedPrice(0)
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
                        .image(marvinS3Uploader.uploadImage(uploadedFile))
                        .userEmail(userEmail)
                        .article(articles)
                        .build();
                imgbox.add(imagePostEntity);

                    imageRepository.save(imagePostEntity);
                }

            }
//        } catch (NullPointerException n) {
//            throw new CustomException(ErrorCode.NullPoint_Token);
//        }

        return ResponseDto.success("작성 성공");

    }

    public ResponseDto<List<ArticleResponseDto>> getArticleCarousel() {
        List<ArticleResponseDto> articleResult = articleRepository.articleCarousel();
        Collections.shuffle(articleResult);

        List<ArticleResponseDto> resultBox = new ArrayList<>();
        if (articleResult.size() < 5) {
            resultBox.addAll(articleResult);
        } else
            for (int i = 0; i < 5; i++) {
                resultBox.add(articleResult.get(i));
            }
        return ResponseDto.success(resultBox);
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


        ArticleDetailResponseDto articleResponseDto = ArticleDetailResponseDto.builder()
                .article(article)
                .isMyArticles(isMyArticles)
                .image(imageBox)
                .category(comfortUtils.getCategoryKorean(article.getCategory()))
                .process(comfortUtils.getProcessKorean(article.getProcess()))
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

    @Transactional
    public ResponseDto<?> patchArticle(List<MultipartFile> multipartFile, ArticleRequestDto articleRequestDto, Long articlesId, UserDetailsImpl userDetails) throws IOException {
        Article targetArticle = articleRepository.findById(articlesId).orElse(null);

        List<String> imageList = articleRequestDto.getImageList();
        for (String s : imageList) {
            imageRepository.deleteByImage(s);
        }
        //        이미지 초기화
//        imageRepository.deleteByArticle_ArticleId(articlesId);

        String userEmail = userDetails.getUsername();

        targetArticle.setTitle(articleRequestDto.getTitle());
        targetArticle.setContent(articleRequestDto.getContent());
        targetArticle.setPrice(articleRequestDto.getPrice());
        targetArticle.setCategory(articleRequestDto.getCategory());


        articleRepository.save(targetArticle);

//        작성시간
//        String time1 = comfortUtils.getTime(articles.getCreatedAt());
//
//        이미지업로드
        if (multipartFile != null) {

            List<Image> imgbox = new ArrayList<>();
            //          이미지 업로드
            for (MultipartFile uploadedFile : multipartFile) {

                Image imagePostEntity = Image.builder()
                        .image(marvinS3Uploader.uploadImage(uploadedFile))
                        .userEmail(userEmail)
                        .article(targetArticle)
                        .build();
                imgbox.add(imagePostEntity);

                imageRepository.save(imagePostEntity);
            }

        }


        return ResponseDto.success("수정 완료");
    }

    public Slice<ArticleResponseDto> getAllArticles(Pageable pageable, Category category, Process process) {

//        CategoryEntity target = categoryRepository.findByUserEmail(userDetails.getMember().getUserEmail());
//        if (target == null) {
//            target = new CategoryEntity(category, process, userDetails.getUsername());
//            System.out.println("target111 = " + target);
//        } else {
//            if (category == null) {
//                target.updateProcess(process);
//            } else if (process == null) {
//                target.updateCategory(category);
//            } else {
//                target.updateProcess(process);
//                target.updateCategory(category);
//            }
//        }
//        CategoryEntity save = categoryRepository.save(target);
        return articleRepository.articleScroll(pageable, category,process);
    }
}