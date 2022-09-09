package com.example.checkcheck.service;

import com.example.checkcheck.dto.requestDto.CommentChoiseRequestDto;
import com.example.checkcheck.dto.requestDto.CommentRequestDto;
import com.example.checkcheck.dto.requestDto.NotificationRequestDto;
import com.example.checkcheck.dto.responseDto.CommentChoiseResponseDto;
import com.example.checkcheck.dto.responseDto.CommentListResponseDto;
import com.example.checkcheck.dto.responseDto.CommentResponseDto;
import com.example.checkcheck.dto.responseDto.ResponseDto;
import com.example.checkcheck.exception.CustomException;
import com.example.checkcheck.exception.ErrorCode;
import com.example.checkcheck.model.AlarmType;
import com.example.checkcheck.model.Member;
import com.example.checkcheck.model.articleModel.Article;
import com.example.checkcheck.model.articleModel.Process;
import com.example.checkcheck.model.commentModel.Comment;
import com.example.checkcheck.repository.ArticleRepository;
import com.example.checkcheck.repository.CommentRepository;
import com.example.checkcheck.repository.MemberRepository;
import com.example.checkcheck.security.UserDetailsImpl;
import com.example.checkcheck.service.notification.NotificationService;
import com.example.checkcheck.util.ComfortUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;
    private final ArticleService articleService;
    private final ComfortUtils comfortUtils;
    private final NotificationService notificationService;


    // 댓글 작성
    @Transactional
    public CommentResponseDto createComment(CommentRequestDto requestDto, UserDetailsImpl userDetails) {

        String userEmail = userDetails.getUsername();
        String nickName = userDetails.getMember().getNickName();
//        댓글 개수  확인용
        Long memberId = userDetails.getMember().getMemberId();

        Optional<Member> memberBox = memberRepository.findByUserEmail(userEmail);
        int userPoint = userDetails.getMember().getPoint() + 1;
        memberBox.get().updatePoint(userPoint);

        String userRank = comfortUtils.getUserRank(memberBox.get().getPoint());

//        댓글 개수 제한
        List<Comment> memberIdCount = commentRepository.findByMember_MemberId(memberId);
        if (memberIdCount.size()>=10) {
            throw new IllegalArgumentException("댓글은 10개 이상 작성이 불가능합니다.");
        }

        // 게시글 확인
        Article article = articleService.isPresentArticle(requestDto.getArticlesId());
        if (null == article) {
            throw new CustomException(ErrorCode.BOARD_NOT_FOUND);
        }
        Comment comment = Comment.builder()
                .comment(requestDto.getComment())
                .nickName(nickName)
                .userRank(userRank)
                .article(article)
                .member(userDetails.getMember())
                .isSelected(false)
                .type(requestDto.getType())
                .build();

        commentRepository.save(comment);

        //해당 댓글로 이동하는 url
        String Url = "http://localhost:8080/api/auth/detail/comments/"+article.getArticleId();

        //댓글 생성 시 게시글 작성 유저에게 실시간 알림 전송 ,
        String message = article.getNickName()+"님! 게시물에 작성된 댓글 알림이 도착했어요!";
        System.out.println("message = " + message);

        //본인의 게시글에 댓글을 남길때는 알림을 보낼 필요가 없다.
        if(!Objects.equals(userDetails.getMember().getMemberId(), article.getMember().getMemberId())) {
            notificationService.send(article.getMember(), AlarmType.COMMENT, message, Url);
            log.info("Alarm message :" + article.getNickName()+"님! 게시물에 작성된 댓글 알림이 도착했어요!");
        }


        boolean isMyComment = false;
        if (comment.getMember().getMemberId().equals(userDetails.getMember().getMemberId())) {
            isMyComment = true;
        }
        String rightNow = comfortUtils.getTime(comment.getCreatedAt());

        return
                CommentResponseDto.builder()
                        .commentsId(comment.getCommentId())
                        .type(comment.getType())
                        .nickName(comment.getNickName())
                        .userRank(comment.getUserRank())
                        .comment(comment.getComment())
                        .createdAt(rightNow)
                        .isSelected(comment.isSelected())
                        .isMyComment(isMyComment)
                        .build();
    }

    // 모든 댓글 조회
    @Transactional
    public CommentListResponseDto readAllComment(Long articlesId, UserDetailsImpl userDetails) {

        // 게시글 확인
//        Article article = articleService.isPresentArticle(articlesId);
        Article article = articleRepository.findById(articlesId).orElse(null);
        if (null == article) {
            throw new NullPointerException("게시글이 존재하지 않습니다");
        }

        List<Comment> commentList = commentRepository.getCommentList(articlesId);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

//        isMyArticles 초기화
        Boolean isMyArticles = false;
        if (article.getMember().getMemberId().equals(userDetails.getMember().getMemberId())) {
            isMyArticles = true;
        }

        for (Comment comment : commentList) {


                String rightNow = comfortUtils.getTime(comment.getCreatedAt());

                Boolean isMyComment = false;
                if (userDetails.getMember().getUserEmail().equals(comment.getMember().getUserEmail())) {
                    isMyComment = true;
                }

                commentResponseDtoList.add(
                        CommentResponseDto.builder()
                                .commentsId(comment.getCommentId())
                                .type(comment.getType())
                                .userRank(comment.getUserRank())
                                .nickName(comment.getNickName())
                                .comment(comment.getComment())
                                .createdAt(rightNow)
                                .isSelected(comment.isSelected())
                                .isMyComment(isMyComment)
                                .build()
                );


            }
        CommentListResponseDto commentListResponseDto = CommentListResponseDto.builder()
                .comments(commentResponseDtoList)
                .isMyArticles(isMyArticles)
                .build();

        return commentListResponseDto;
    }

    // 댓글 채택


    // 댓글 삭제
    @Transactional
    public ResponseDto<?> deleteComment(Long commentsId, Member member) {

        Comment comment = isPresentComment(commentsId);
        if (null == comment) {
            return ResponseDto.fail("NOT_FOUND", "댓글이 존재하지 않습니다.");
        }

        if (comment.getMember().equals(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 삭제 할 수 있습니다.");
        }

        commentRepository.delete(comment);
        return ResponseDto.success("success");
    }

    // 댓글 번호 확인
    @Transactional
    public Comment isPresentComment(Long id) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        return optionalComment.orElse(null);
    }


    public CommentChoiseResponseDto commentChoose(Long articlesId, CommentChoiseRequestDto commentChoiseRequestDto, UserDetailsImpl userDetails) {
        Long commentsId = commentChoiseRequestDto.getCommentsId();

        Article targetArticle = articleRepository.findById(articlesId).orElseThrow(
                () -> new NullPointerException("게시글이 존재하지않습니다.")
        );

        Comment targetComment = commentRepository.findById(commentsId).orElseThrow(
                () -> new NullPointerException("채택할 댓글이 없습니다.")
        );

        if (!targetArticle.getMember().getUserEmail().equals(userDetails.getUsername())) {
            throw new IllegalArgumentException("게시글 작성자만 채택할수있습니다.");
        }

        if (targetArticle.getUserEmail().equals(targetComment.getMember().getUserEmail())) {
            throw new IllegalArgumentException("게시글 작성자와 댓글 작성자가 같습니다.");
        }

        if (targetArticle.getProcess().equals(Process.done)) {
            throw new IllegalArgumentException("채택된 댓글이 있어서 채택할수없습니다.");
        }

//      채택 댓글 포인트
        Optional<Member> targetMember = memberRepository.findById(targetComment.getMember().getMemberId());
        int point = targetComment.getMember().getPoint();
        targetMember.get().updatePoint(point + 50);

        System.out.println("point = " + point);
//        게시글 상태 변경
        targetArticle.updateProcess(Process.done);
//        댓글 상태 변경
        targetComment.chooseComment(true);
//        저장
        commentRepository.save(targetComment);
        articleRepository.save(targetArticle);

//        로그인 사용자와 채택댓글 작성자 비교
        boolean isMyComment = false;
        if (userDetails.getUsername().equals(targetComment.getMember().getUserEmail())) {
            isMyComment = true;
        }
        //해당 댓글로 이동하는 url
        String Url = "http://localhost:8080/api/auth/detail/comments/"+targetComment.getMember().getMemberId();

        //댓글 채택 시 채택된 댓글 유저에게 실시간 알림 전송
        String message = targetComment.getNickName()+"님! 게시물에 작성된 댓글이 채택되었어요, +50 포인트를 획득하셨습니다, 축하드립니다!";

        //로그인 사용자와 채택댓글 작성자가 다를 경우에는 알림을 보낼 필요가 없다.
        if(!Objects.equals(userDetails.getMember().getMemberId(), targetComment.getMember().getMemberId())) {
            notificationService.send(targetComment.getMember(), AlarmType.CHOICE, message, Url);
            log.info("Alarm message :" + targetComment.getNickName()+"님! 게시물에 작성된 댓글이 채택되었어요, 축하합니다!");
        }
//        댓글 작성자 랭크
        String userRank = comfortUtils.getUserRank(targetMember.get().getPoint());


        return CommentChoiseResponseDto.builder()
                .comment(targetComment)
                .article(targetArticle)
                .isMyComment(isMyComment)
                .isSelected(true)
                .process(comfortUtils.getProcessKorean(targetArticle.getProcess()))
                .commentsUserRank(userRank)
                .build();
    }
//    Optional<Member> targetMember = memberRepository.findById(comment.getMember().getMemberId());
//    int point = comment.getMember().getPoint();
//        targetMember.get().updatePoint(point+1);

}