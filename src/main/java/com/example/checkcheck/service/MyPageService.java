package com.example.checkcheck.service;

import com.example.checkcheck.dto.responseDto.MyPageMemberResponseDto;
import com.example.checkcheck.dto.responseDto.MyPageResponseDto;
import com.example.checkcheck.dto.responseDto.ResponseDto;
import com.example.checkcheck.model.Member;
import com.example.checkcheck.model.RefreshToken;
import com.example.checkcheck.model.articleModel.Article;
import com.example.checkcheck.model.articleModel.Process;
import com.example.checkcheck.repository.ArticleRepository;
import com.example.checkcheck.repository.MemberRepository;
import com.example.checkcheck.repository.RefreshTokenRepository;
import com.example.checkcheck.security.UserDetailsImpl;
import com.example.checkcheck.util.ComfortUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MyPageService {

    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;
    private final ComfortUtils comfortUtils;
    private final RefreshTokenRepository refreshTokenRepository;

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
//                      유저 실제 이메일 조회
                        .userEmail(memberBox.get().getUserRealEmail())
                        .userRank(userRank)
                        .userPoint(userPoint)
                        .articleCount(articleCount)
                        .build()
        );
    }

    // 마이페이지 작성 게시글 조회
    @Transactional
    public ResponseDto<?> readMyPageArticle(UserDetailsImpl userDetails, Process process) {

        return ResponseDto.success(articleRepository.myPageInfo(userDetails, process));
    }

    // 회원 탈퇴
    @Transactional
    public ResponseDto<?> deleteMember(UserDetailsImpl userDetails) {
        Member member = userDetails.getMember();
        if (null == member) {
            return ResponseDto.fail("400", "회원 정보가 존재하지 않습니다.");
        }
        memberRepository.deleteById(member.getMemberId());
        refreshTokenRepository.deleteByTokenKey(member.getUserEmail());

        return ResponseDto.success("탈퇴 완료");
    }

}
