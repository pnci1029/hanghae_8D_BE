package com.example.checkcheck.service;

import com.example.checkcheck.dto.requestDto.ArticleRequestDto;
import com.example.checkcheck.model.Article;
import com.example.checkcheck.model.Category;
import com.example.checkcheck.model.Member;
import com.example.checkcheck.repository.ArticleRepository;
import com.example.checkcheck.repository.ImageRepository;
import com.example.checkcheck.repository.MemberRepository;
import com.example.checkcheck.security.UserDetailsImpl;
import com.example.checkcheck.service.s3.S3Uploader;
import com.example.checkcheck.util.ComfortUtils;
import com.example.checkcheck.util.Time;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    public ResponseEntity<Object> postArticles(List<MultipartFile> multipartFile, ArticleRequestDto articleRequestDto,
                                              UserDetailsImpl userDetails) throws IOException {

        String nickName = userDetails.getMember().getNickName();
        String userEmail = userDetails.getUsername();


        //        유저 포인트
        Optional<Member> memberBox = memberRepository.findByUserEmail(userEmail);
        int userPoint= userDetails.getMember().getPoint()+2;
        memberBox.get().updatePoint(userPoint);

        System.out.println("articleRequestDto = " + articleRequestDto.getCategory());

        //        게시글
        Article articles = Article.builder()
                .nickName(nickName)
                .title(articleRequestDto.getTitle())
                .content(articleRequestDto.getContent())
                .price(articleRequestDto.getPrice())
                .category(articleRequestDto.getCategory())
                .build();
        articleRepository.save(articles);

        String time1 = comfortUtils.getTime(articles.getCreatedAt());
        System.out.println(time1);

//        이미지업로드
//        if (multipartFile != null) {
//
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

}
