package com.example.checkcheck.service;

import com.example.checkcheck.dto.requestDto.NickNameRequestDto;
import com.example.checkcheck.dto.responseDto.MyPageMemberResponseDto;
import com.example.checkcheck.dto.responseDto.ResponseDto;
import com.example.checkcheck.exception.CustomException;
import com.example.checkcheck.exception.ErrorCode;
import com.example.checkcheck.model.Member;
import com.example.checkcheck.model.articleModel.Article;
import com.example.checkcheck.model.articleModel.Process;
import com.example.checkcheck.repository.ArticleRepository;
import com.example.checkcheck.repository.MemberRepository;
import com.example.checkcheck.repository.RefreshTokenRepository;
import com.example.checkcheck.security.UserDetailsImpl;
import com.example.checkcheck.util.ComfortUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

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
            throw new CustomException(ErrorCode.NOT_EXIST_CLIENT);
        }
        memberRepository.deleteById(member.getMemberId());
        refreshTokenRepository.deleteByTokenKey(member.getUserEmail());

        return ResponseDto.success("탈퇴 완료");
    }

    public ResponseDto<?> changeNickName(NickNameRequestDto nickName, UserDetailsImpl userDetails) {

//        Boolean word = Pattern.matches("^[0-9a-zA-Zㄱ-ㅎ가-힣]*$",nickName);
        if (!Pattern.matches("^[0-9a-zA-Zㄱ-ㅎ가-힣]*$", nickName.getNickName())) {
            throw new CustomException(ErrorCode.NICKNAME_TYPE_EXCEPTION);
        }

        Optional<Member> targetNickName = memberRepository.findByNickName(nickName.getNickName());
        if (targetNickName.isPresent()) {
            throw new CustomException(ErrorCode.EXIST_NICKNAME);
        }
//        글자수 1~6자
        if (nickName.getNickName().length() < 1 || nickName.getNickName().length() > 7) {
            throw new CustomException(ErrorCode.NICKNAME_EXCEPTION);
        }
        Member targetMember = memberRepository.findByUserEmail(userDetails.getUsername()).orElse(null);
        targetMember.updateNickName(nickName.getNickName());
        memberRepository.save(targetMember);

//        게시글에 있는 유저닉네임도 수정
        List<Article> targetArticles = articleRepository.findByUserEmail(targetMember.getUserEmail());
        for (Article targetArticle : targetArticles) {
            targetArticle.setNickName(nickName.getNickName());
        }
        articleRepository.saveAll(targetArticles);


        return ResponseDto.success("닉네임이 변경 되었습니다.");
    }
}
