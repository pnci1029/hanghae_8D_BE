package com.example.checkcheck.service;

import com.example.checkcheck.dto.requestDto.NickNameRequestDto;
import com.example.checkcheck.dto.responseDto.MyPageMemberResponseDto;
import com.example.checkcheck.dto.responseDto.MyPageResponseDto;
import com.example.checkcheck.dto.responseDto.ResponseDto;
import com.example.checkcheck.exception.CustomException;
import com.example.checkcheck.exception.ErrorCode;
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

@Service
public class MyPageService {
    private MemberRepository memberRepository;

    private ArticleRepository articleRepository;
    private ComfortUtils comfortUtils;
    private RefreshTokenRepository refreshTokenRepository;
    public MyPageService(MemberRepository memberRepository, ArticleRepository articleRepository, ComfortUtils comfortUtils, RefreshTokenRepository refreshTokenRepository) {
        this.memberRepository = memberRepository;
        this.articleRepository = articleRepository;
        this.comfortUtils = comfortUtils;
        this.refreshTokenRepository = refreshTokenRepository;
    }

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
                        .userName(userDetails.getMember().getUserName())
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

    public ResponseDto<?> changeNickName(NickNameRequestDto nickName, UserDetailsImpl userDetails) {

        Optional<Member> targetNickName = memberRepository.findByNickName(nickName.getNickName());
        if (targetNickName.isPresent()) {
            throw new CustomException(ErrorCode.EXIST_NICKNAME);
        }
//        글자수 1~6자
        if (nickName.getNickName().length() < 1 || nickName.getNickName().length() > 6) {
            throw new CustomException(ErrorCode.NICKNAME_EXCEPTION);
        }
        Member targetMember = memberRepository.findByUserEmail(userDetails.getUsername()).orElse(null);
        targetMember.updateNickName(nickName.getNickName());
        memberRepository.save(targetMember);

        return ResponseDto.success("닉네임이 변경 되었습니다.");
    }
}
