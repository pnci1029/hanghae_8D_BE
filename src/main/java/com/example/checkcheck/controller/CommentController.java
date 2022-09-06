package com.example.checkcheck.controller;

import com.example.checkcheck.dto.requestDto.CommentChoiseRequestDto;
import com.example.checkcheck.dto.requestDto.CommentRequestDto;
import com.example.checkcheck.dto.responseDto.CommentChoiseResponseDto;
import com.example.checkcheck.dto.responseDto.ResponseDto;
import com.example.checkcheck.security.UserDetailsImpl;
import com.example.checkcheck.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("/api/auth/detail/comments/{articlesId}")
    public ResponseDto<?> createComment(@PathVariable Long articlesId, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.createComment(articlesId, requestDto, userDetails.getMember());

        //        return ResponseEntity.status(HttpStatus.OK)
//                .contentType(new MediaType("applicaton", "text", StandardCharsets.UTF_8))
//                .body(message);
//        rsp.setContentType(String.valueOf(new MediaType("applicaton", "text", StandardCharsets.UTF_8)));
    }

    // 댓글 조회
    @GetMapping("/api/auth/detail/comments/{articlesId}")
    public ResponseDto<?> readAllComment(@PathVariable Long articlesId) {
        return commentService.readAllComment(articlesId);
    }


    // 댓글 삭제
    @DeleteMapping("/api/auth/detail/comments/{commentsId}")
    public ResponseDto<?> deleteComment(@PathVariable Long commentsId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.deleteComment(commentsId, userDetails.getMember());
    }





























    // 댓글 채택
    @PatchMapping("/api/auth/detail/comments/{articlesId}")
    public CommentChoiseResponseDto choose(@PathVariable Long articlesId , @RequestBody CommentChoiseRequestDto commentChoiseRequestDto,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
       return commentService.commentChoose(articlesId,commentChoiseRequestDto, userDetails);
    }
}
