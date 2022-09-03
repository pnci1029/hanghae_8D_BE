package com.example.checkcheck.testrunner;

import com.example.checkcheck.model.Member;
import com.example.checkcheck.model.articleModel.Article;
import com.example.checkcheck.model.articleModel.Category;
import com.example.checkcheck.model.articleModel.Process;
import com.example.checkcheck.repository.ArticleRepository;
import com.example.checkcheck.repository.MemberRepository;
import com.example.checkcheck.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@RequiredArgsConstructor
@Component
public class TestDataRunner implements ApplicationRunner {

    MemberService userService;


    MemberRepository userRepository;

    PasswordEncoder passwordEncoder;
    ArticleRepository articleRepository;

    @Autowired
    public TestDataRunner(MemberService userService, MemberRepository userRepository, PasswordEncoder passwordEncoder, ArticleRepository articleRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.articleRepository = articleRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
// 테스트 User 생성
        Member testUser = new Member("1234", "1234121","k_1234@naver.com", LocalDateTime.now(),"true",null, "1234@gmail.com");
        userRepository.save(testUser);

        for (int i = 0; i < 42; i++) {
            Article articles = Article.builder()
                    .nickName(testUser.getNickName())
                    .title("1111")
                    .content("1234")
                    .price(14141)
                    .category(Category.etc)
                    .process(Process.process)
                    .userRank("S")
                    .build();
            articleRepository.save(articles);
        }

    }
}