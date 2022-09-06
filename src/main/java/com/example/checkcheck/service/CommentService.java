package com.example.checkcheck.service;

import com.example.checkcheck.dto.requestDto.CommentChoiseRequestDto;
import com.example.checkcheck.dto.requestDto.CommentRequestDto;
import com.example.checkcheck.dto.requestDto.NotificationRequestDto;
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
import com.example.checkcheck.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;
    private final ArticleService articleService;

    private final NotificationService notificationService;


    // 댓글 작성
    @Transactional
    public CommentResponseDto createComment(CommentRequestDto requestDto, Member member) {

        // 게시글 확인
        Article article = articleService.isPresentArticle(requestDto.getArticleId());
        if (null == article) {
            throw new CustomException(ErrorCode.BOARD_NOT_FOUND);
        }

        Comment comment = Comment.builder()
                .comment(requestDto.getComment())
                .article(article)
                .member(member)
                .isSelected(false)
                .type(requestDto.getType())
                .build();

        commentRepository.save(comment);

        if (!article.getMember().getUserEmail().equals(member.getUserEmail())) {
            NotificationRequestDto notificationRequestDto =
                    new NotificationRequestDto(
                            AlarmType.COMMENT, "새로운 댓글이 작성되었습니다!"
                    );
            System.out.println(article.getMember().getMemberId());
            notificationService.sendNotification(article.getMember().getMemberId(), notificationRequestDto);
            //85번 article (1번유저) -> 86번 article(2번유저) - 1번유저의 코멘트 -> 2번 유저에게 전달

        }
            return
            CommentResponseDto.builder()
                .commentId(comment.getCommentId())
                .type(comment.getType())
                .comment(comment.getComment())
                .createdAt(comment.getCreatedAt())
                .build();


    }

    // 모든 댓글 조회
    @Transactional
    public ResponseDto<?> readAllComment(Long articlesId) {
        // 게시글 확인
        Article article = articleService.isPresentArticle(articlesId);
        if (null == article) {
            return ResponseDto.fail("NOT_FOUND", "게시물이 존재하지 않습니다.");
        }

        List<Comment> commentList = commentRepository.findAllByArticle(article);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        for (Comment comment : commentList) {
            commentResponseDtoList.add(
                    CommentResponseDto.builder()
                            .commentId(comment.getCommentId())
                            .type(comment.getType())
                            .comment(comment.getComment())
                            .createdAt(comment.getCreatedAt())
//                            .isSelected(comment.getIsSelected())
                            .build()
            );
        }
        return ResponseDto.success(commentResponseDtoList);
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





































    public void commentChoose(Long articlesId,CommentChoiseRequestDto commentChoiseRequestDto) {
        Long commentsId = commentChoiseRequestDto.getCommentsId();
        Comment targetComment = commentRepository.findById(commentsId).orElseThrow(
                () -> new NullPointerException("채택할 댓글이 없습니다.")
        );


        Article targetArticle = articleRepository.findById(articlesId).orElseThrow(
                () -> new NullPointerException("게시글이 존재하지않습니다")
        );

        Optional<Member> targetMember = memberRepository.findById(targetComment.getMember().getMemberId());
        int point = targetComment.getMember().getPoint();
        targetMember.get().updatePoint(point+50);

        targetArticle.setProcess(Process.done);

        targetComment.chooseComment(true);
        System.out.println("target = " + targetComment.getIsSelected());
        commentRepository.save(targetComment);
        articleRepository.save(targetArticle);


    }
//    Optional<Member> targetMember = memberRepository.findById(comment.getMember().getMemberId());
//    int point = comment.getMember().getPoint();
//        targetMember.get().updatePoint(point+1);

}
