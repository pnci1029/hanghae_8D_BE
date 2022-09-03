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

    public ArticleService(ArticleRepository articleRepository, ImageRepository imageRepository, MemberRepository memberRepository, S3Uploader s3Uploader, ComfortUtils comfortUtils, Time time) {
        this.articleRepository = articleRepository;
        this.imageRepository = imageRepository;
        this.memberRepository = memberRepository;
        this.s3Uploader = s3Uploader;
        this.comfortUtils = comfortUtils;
        this.time = time;
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

//        이미지업로드
//        if (multipartFile != null) {
//
//            List<Image> imgbox = new ArrayList<>();
//            //          이미지 업로드
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
//
//        }

        return ResponseEntity.ok().build();
    }

//    @Transactional
//    public Slice<ArticleResponseDto> showAllArticle(Category category, Process process, int size, int page) {
//        List<ArticleResponseDto> resultList = new ArrayList<>();
//
////        전체 조회
//        if (process.toString().equals("all")) {
//            List<Article> articleList = articleRepository.findByCategory(category);
//            for (Article article : articleList) {
//                ArticleResponseDto articleResponseDto = ArticleResponseDto.builder()
//                        .article(article)
//                        .build();
//                resultList.add(articleResponseDto);
//            }
//
////            진행상태에 따른 조회
//        } else {
//            List<Article> articleList = articleRepository.findByCategoryAndProcess(category, process);
//            for (Article article : articleList) {
//                List<String> imageBox = new ArrayList<>();
//
//                List<Image> imageList = imageRepository.findByArticle_ArticleId(article.getArticleId());
//                for (Image image : imageList) {
//                    imageBox.add(image.getImage());
//                }
//                ArticleResponseDto articleResponseDto = ArticleResponseDto.builder()
//                        .article(article)
//                        .image(imageBox)
//                        .build();
//                resultList.add(articleResponseDto);
//            }
//        }
//
//
////            return resultList;
//        return null;
//    }
}
