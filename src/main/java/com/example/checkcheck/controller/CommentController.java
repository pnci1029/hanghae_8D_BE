package com.example.checkcheck.controller;

import com.example.checkcheck.dto.requestDto.CommentChoiseRequestDto;
import com.example.checkcheck.dto.requestDto.CommentRequestDto;
import com.example.checkcheck.dto.responseDto.CommentChoiseResponseDto;
import com.example.checkcheck.dto.responseDto.CommentResponseDto;
import com.example.checkcheck.dto.responseDto.ResponseDto;
import com.example.checkcheck.dto.responseDto.StatusResponseDto;
import com.example.checkcheck.security.UserDetailsImpl;
import com.example.checkcheck.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("/api/auth/detail/comments")
    public ResponseEntity<Object> createComment(@RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CommentResponseDto commentResponseDto = commentService.createComment(requestDto, userDetails);
        return new ResponseEntity<>(new StatusResponseDto("댓글 등록 성공", commentResponseDto), HttpStatus.OK);

        //        return ResponseEntity.status(HttpStatus.OK)
//                .contentType(new MediaType("applicaton", "text", StandardCharsets.UTF_8))
//                .body(message);
//        rsp.setContentType(String.valueOf(new MediaType("applicaton", "text", StandardCharsets.UTF_8)));
    }

    // 댓글 조회
    @GetMapping("/api/auth/detail/comments/{articlesId}")
    public ResponseDto<?> readAllComment(@PathVariable Long articlesId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.readAllComment(articlesId, userDetails);
    }


    // 댓글 삭제
    @DeleteMapping("/api/auth/detail/comments/{commentsId}")
    public ResponseDto<?> deleteComment(@PathVariable Long commentsId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.deleteComment(commentsId, userDetails.getMember());
    }




























    // 댓글 채택
    @PatchMapping("/api/auth/detail/comments/{articlesId}")
    public ResponseEntity<Object> choose(@PathVariable Long articlesId ,@RequestBody CommentChoiseRequestDto commentChoiseRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CommentChoiseResponseDto commentChoiseResponseDto = commentService.commentChoose(articlesId,commentChoiseRequestDto, userDetails);
        return new ResponseEntity<>(new StatusResponseDto("댓글 채택 성공", commentChoiseResponseDto), HttpStatus.OK);
    }
}
