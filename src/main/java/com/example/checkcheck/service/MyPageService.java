package com.example.checkcheck.service;

import com.example.checkcheck.dto.responseDto.MyPageMemberResponseDto;
import com.example.checkcheck.dto.responseDto.MyPageResponseDto;
import com.example.checkcheck.dto.responseDto.ResponseDto;
import com.example.checkcheck.model.Member;
import com.example.checkcheck.model.articleModel.Article;
import com.example.checkcheck.repository.ArticleRepository;
import com.example.checkcheck.repository.MemberRepository;
import com.example.checkcheck.security.UserDetailsImpl;
import com.example.checkcheck.util.ComfortUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MyPageService {

    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;
    private final ArticleService articleService;
    private final ComfortUtils comfortUtils;

    // 마이페이지 회원 정보 조회
    @Transactional
    public ResponseDto<?> readMyPage(UserDetailsImpl userDetails) {

        String userEmail = userDetails.getUsername();

        Optional<Member> memberBox = memberRepository.findByUserEmail(userEmail);
        int userPoint = userDetails.getMember().getPoint();
        memberBox.get().updatePoint(userPoint);

        String userRank = comfortUtils.getUserRank(memberBox.get().getPoint());

        int articleCount = articleRepository.findAllByMember(userDetails.getMember()).size();

        return ResponseDto.success(
                MyPageMemberResponseDto.builder()
                        .nickName(userDetails.getMember().getNickName())
                        .userEmail(userEmail)
                        .userRank(userRank)
                        .userPoint(userPoint)
                        .articleCount(articleCount)
                        .build()
        );
    }

    // 마이페이지 작성 게시글 조회
    @Transactional
    public ResponseDto<?> readMyPageArticle(Long articleId, UserDetailsImpl userDetails) {
        Article article = articleService.isPresentArticle(articleId);
        if (null == article) {
            return ResponseDto.fail("400", "게시물이 존재하지 않습니다.");
        }
        Member member = userDetails.getMember();
        if (!article.getMember().getMemberId().equals(member.getMemberId())) {
            return ResponseDto.fail("400", "회원 정보가 일치하지 않습니다.");
        }

        return ResponseDto.success(
                MyPageResponseDto.builder()
                        .articlesId(article.getArticleId())
                        .title(article.getTitle())
                        .process(article.getProcess())
                        .price(article.getPrice())
                        .image(article.getImages())
                        .point(member.getPoint())
                        .build()
        );
    }



//    // 회원 탈퇴
//    @Transactional
//    public ResponseDto<?> deleteMember(Long memberId, UserDetailsImpl userDetails) {
//
////        Member checkMember = userDetails.getMember();
//
//        Member member = memberRepository.findById(memberId).orElseThrow(
//                () -> new NullPointerException("회원 정보가 없습니다.")
//        );
//
//        if (!member.getMemberId().equals(userDetails.getMember().getMemberId())) {
//            return ResponseDto.fail("400", "회원 정보가 일치하지 않습니다.");
//        }
//        memberRepository.deleteById(member.getMemberId());
//
//        return ResponseDto.success("탈퇴 완료");
//    }

    // 회원 탈퇴
    @Transactional
    public ResponseDto<?> deleteMember(UserDetailsImpl userDetails) {
        Member member = userDetails.getMember();
        memberRepository.deleteById(member.getMemberId());
        return ResponseDto.success("탈퇴 완료");
    }

}
