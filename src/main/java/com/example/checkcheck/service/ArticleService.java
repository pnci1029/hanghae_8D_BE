package com.example.checkcheck.service;

import com.example.checkcheck.dto.requestDto.ArticleRequestDto;
import com.example.checkcheck.dto.responseDto.ArticleDetailResponseDto;
import com.example.checkcheck.dto.responseDto.ArticleResponseDto;
import com.example.checkcheck.dto.responseDto.ResponseDto;
import com.example.checkcheck.exception.CustomException;
import com.example.checkcheck.exception.ErrorCode;
import com.example.checkcheck.model.Image;
import com.example.checkcheck.model.Member;
import com.example.checkcheck.model.articleModel.Article;
import com.example.checkcheck.model.articleModel.Process;
import com.example.checkcheck.repository.ArticleRepository;
import com.example.checkcheck.repository.ImageRepository;
import com.example.checkcheck.repository.MemberRepository;
import com.example.checkcheck.security.UserDetailsImpl;
import com.example.checkcheck.service.notification.NotificationService;
import com.example.checkcheck.service.s3.ImgScalrS3Uploader;
import com.example.checkcheck.util.ComfortUtils;
import com.example.checkcheck.util.Time;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ArticleService {

    private ArticleRepository articleRepository;
    private ImageRepository imageRepository;
    private MemberRepository memberRepository;
    //    private MarvinS3Uploader marvinS3Uploader;
    private ImgScalrS3Uploader imgScalrS3Uploader;
    private ComfortUtils comfortUtils;
    private Time time;


    private NotificationService notificationService;

    public ArticleService(
            ArticleRepository articleRepository,
            ImageRepository imageRepository,
            MemberRepository memberRepository,
            ImgScalrS3Uploader imgScalrS3Uploader,
//            MarvinS3Uploader marvinS3Uploader,
            ComfortUtils comfortUtils,
            Time time,
            NotificationService notificationService
    ) {
        this.articleRepository = articleRepository;
        this.imageRepository = imageRepository;
        this.memberRepository = memberRepository;
        this.imgScalrS3Uploader = imgScalrS3Uploader;
//        this.marvinS3Uploader = marvinS3Uploader;
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
        comfortUtils.badWordsFilter(articleRequestDto.getTitle());

        //        유저 포인트
        Optional<Member> memberBox = memberRepository.findByUserEmail(userEmail);
        int userPoint = userDetails.getMember().getPoint() + 2;
        memberBox.get().updatePoint(userPoint);

        String userRank = comfortUtils.getUserRank(memberBox.get().getPoint());

//        dto 널값 예외처리
        if (Objects.equals(articleRequestDto.getTitle(), "")) {
            throw new CustomException(ErrorCode.NULL_ARTICLE_TILE);
        }
        if (Objects.equals(articleRequestDto.getContent(), "")) {
            throw new CustomException(ErrorCode.NULL_ARTICLE_CONTENT);
        }
        if (articleRequestDto.getPrice() == 0) {
            throw new CustomException(ErrorCode.NULL_ARTICLE_PRICE);
        }
//            if (Objects.equals(String.valueOf(articleRequestDto.getCategory()), null)) {
        if (Objects.equals(articleRequestDto.getCategory(), "")) {
            throw new CustomException(ErrorCode.NULL_ARTICLE_CATEGORY);
        }

        //        게시글
        Article articles = Article.builder()
                .nickName(nickName)
                .title(articleRequestDto.getTitle())
                .content(articleRequestDto.getContent())
                .price(articleRequestDto.getPrice())
//                    .category(articleRequestDto.getCategory())
//                .category(Category.valueOf(articleRequestDto.getCategory()))
                .category(articleRequestDto.getCategory())
                .process(Process.process)
                .userRank(userRank)
                .member(userDetails.getMember())
                .userEmail(userEmail)
                .selectedPrice(0)
                .build();

        ////        이미지업로드
        List<Image> imgbox = new ArrayList<>();
        //          이미지 업로드
        for (MultipartFile uploadedFile : multipartFile) {
            try {

                Image imagePostEntity = Image.builder()
                        .image(imgScalrS3Uploader.uploadImage(uploadedFile))
                        .userEmail(userEmail)
                        .article(articles)
                        .build();
                imgbox.add(imagePostEntity);


                imageRepository.save(imagePostEntity);
            } catch (NullPointerException e) {
                throw new CustomException(ErrorCode.NO_IMAGE_EXCEPTION);
            }
        }
//            if (multipartFile != null) {

        articleRepository.save(articles);
        return ResponseDto.success("작성 성공");
//            } else {
//                throw new CustomException(ErrorCode.NO_IMAGE_EXCEPTION);
//            }

//        } catch (NullPointerException e) {
//            throw new CustomException(ErrorCode.DUPLE_EMAIL);
//        }

    }

    //    public ResponseDto<List<ArticleResponseDto>> getArticleCarousel() {
    public ResponseDto<List<ArticleResponseDto>> getArticleCarousel() {
        List<ArticleResponseDto> articleResult = articleRepository.articleCarousel();
        Collections.shuffle(articleResult);

        List<ArticleResponseDto> resultBox = new ArrayList<>();
        if (articleResult.size() < 10) {
            resultBox.addAll(articleResult);
        } else
            for (int i = 0; i < 10; i++) {
                resultBox.add(articleResult.get(i));
            }
        return ResponseDto.success(resultBox);
    }

    //  게시글 상세페이지
    public ArticleDetailResponseDto getArticleDetail(Long id, UserDetailsImpl userDetails) {
        Article article = articleRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.ARTICLE_NOT_FOUND)
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

//        작성시간
        String createdAt = article.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
//      게시글 채택상태일때
        if (article.getProcess().equals(Process.done)) {
            ArticleDetailResponseDto articleResponseDto = ArticleDetailResponseDto.builder()
                    .article(article)
                    .price(null)
                    .selectedPrice(NumberFormat.getInstance().format(article.getSelectedPrice()))
                    .isMyArticles(isMyArticles)
                    .image(imageBox)
//                    .category(comfortUtils.getCategoryKorean(article.getCategory()))
                    .category(article.getCategory())
                    .process(comfortUtils.getProcessKorean(article.getProcess()))
                    .createdAt(createdAt)
                    .build();
            return articleResponseDto;
        }

//          게시글 진행상태일때
        ArticleDetailResponseDto articleResponseDto = ArticleDetailResponseDto.builder()
                .article(article)
                .isMyArticles(isMyArticles)
                .price(NumberFormat.getInstance().format(article.getPrice()))
                .selectedPrice(null)
                .image(imageBox)
//                .category(comfortUtils.getCategoryKorean(article.getCategory()))
                .category(article.getCategory())
                .process(comfortUtils.getProcessKorean(article.getProcess()))
                .createdAt(createdAt)
                .build();


        return articleResponseDto;
    }

    @Transactional
    public ResponseDto<?> deleteArticle(Long articlesId, UserDetailsImpl userDetails) {
        Optional<Article> target = articleRepository.findById(articlesId);

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

//        dto 널값 예외처리
        if (articleRequestDto.getTitle().isEmpty()) {
            throw new CustomException(ErrorCode.NULL_ARTICLE_TILE);
        }
        if (articleRequestDto.getContent().isEmpty()) {
            throw new CustomException(ErrorCode.NULL_ARTICLE_CONTENT);
        }
        if (String.valueOf(articleRequestDto.getPrice()).isEmpty()) {
            throw new CustomException(ErrorCode.NULL_ARTICLE_PRICE);
        }
        if (Objects.equals(articleRequestDto.getCategory(), "")) {
            throw new CustomException(ErrorCode.NULL_ARTICLE_CATEGORY);
        }
        if (!userDetails.getMember().getUserEmail().equals(targetArticle.getMember().getUserEmail())) {
            throw new CustomException(ErrorCode.NOT_MY_ARTICLE);
        }

        List<String> imageList = articleRequestDto.getImageList();
        for (String s : imageList) {
            imageRepository.deleteByImage(s);
        }

        String userEmail = userDetails.getUsername();

        targetArticle.setTitle(articleRequestDto.getTitle());
        targetArticle.setContent(articleRequestDto.getContent());
        targetArticle.setPrice(articleRequestDto.getPrice());
//        targetArticle.setCategory(articleRequestDto.getCategory());
        targetArticle.setCategory(articleRequestDto.getCategory());


        articleRepository.save(targetArticle);

//        이미지업로드
        List<Image> imgbox = new ArrayList<>();
        if (multipartFile == null) {
            return ResponseDto.success("수정 완료");

        } else {
            for (MultipartFile uploadedFile : multipartFile) {


                    Image imagePostEntity = Image.builder()
                            .image(imgScalrS3Uploader.uploadImage(uploadedFile))
                            .userEmail(userEmail)
                            .article(targetArticle)
                            .build();
                    imgbox.add(imagePostEntity);
                    imageRepository.save(imagePostEntity);
            }

        }


        return ResponseDto.success("수정 완료");
    }

    public Slice<ArticleResponseDto> getAllArticles(Pageable pageable, String category, Process process) {
        return articleRepository.articleScroll(pageable, category, process);
    }
}