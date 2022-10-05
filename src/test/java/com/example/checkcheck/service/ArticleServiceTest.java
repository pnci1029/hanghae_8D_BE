package com.example.checkcheck.service;

import com.example.checkcheck.dto.responseDto.ArticleResponseDto;
import com.example.checkcheck.exception.CustomException;
import com.example.checkcheck.exception.ErrorCode;
import com.example.checkcheck.model.Member;
import com.example.checkcheck.model.articleModel.Article;
import com.example.checkcheck.model.articleModel.Process;
import com.example.checkcheck.model.commentModel.Comment;
import com.example.checkcheck.model.commentModel.Type;
import com.example.checkcheck.repository.ArticleRepository;
import com.example.checkcheck.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Service
public class ArticleServiceTest {
    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    CommentRepository commentRepository;

    public List<Article> create(String title, String content, String category, int price, Member member) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("제목을 입력해주세요.");
        }
        List<Article> result = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            Article article = Article.builder()
                    .nickName(member.getNickName())
                    .title(title)
                    .content(content)
                    .price(price)
                    .category(category)
                    .process(Process.process)
                    .userRank("S")
                    .member(member)
                    .userEmail(member.getUserEmail())
                    .selectedPrice(0)
                    .build();
            articleRepository.save(article);
            result.add(article);
        }
        return result;
    }

    public Slice<?> getAll(Pageable pageable, String category, Process process) {

        return null;
    }

    public List<ArticleResponseDto> getCarousel() {
//        진행중인 상태만 조회
        List<ArticleResponseDto> resultBox = new ArrayList<>();
        List<Article> result = articleRepository.findAll();
        for (Article article : result) {
            ArticleResponseDto articleResponseDto = ArticleResponseDto.builder()
                    .article(article)
                    .process(String.valueOf(Process.process))
                    .userRank("S")
                    .price(String.valueOf(article.getPrice()))
                    .selectedPrice("0")
                    .image(null)
                    .build();
            resultBox.add(articleResponseDto);
        }
        return resultBox;
    }

    public Article patch(long articlesId, String title, String content, int price, String category) {
        Optional<Article> result = articleRepository.findById(articlesId);
        if (articlesId == 0) {
            throw new NullPointerException("존재하지 않는 게시글입니다");
        }
        if (title.equals("")) {
            throw new NullPointerException("제목을 입력해주세요.");
        }

        result.get().setTitle(title);
        result.get().setPrice(price);
        result.get().setCategory(category);
        result.get().setContent(content);
        return result.orElse(null);
    }

    public String delete(long articlesId) {
        Optional<Article> target = articleRepository.findById(articlesId);
        if (target.isEmpty()) {
            throw new NullPointerException("존재하지 않는  게시글입니다.");
        }
        return "삭제가 완료되었습니다.";
    }


//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ댓글ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ//
//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ댓글ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ//
//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ댓글ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ//

    public Comment createComment(long articlesId, String comment, Type type) {
        Optional<Article> target = articleRepository.findById(articlesId);

        if (target.isEmpty()) {
            throw new NullPointerException("게시글이 존재하지 않습니다.");
        }
        if (comment.isEmpty()) {
            throw new IllegalArgumentException("댓글을 작성해주세요");
        }
        Comment comments = null;
        for (int i = 0; i < 11; i++) {
            comments = Comment.builder()
                    .comment(comment)
                    .type(type)
                    .article(target.get())
                    .member(target.get().getMember())
                    .nickName(target.get().getMember().getNickName())
                    .userRank("S")
                    .isMyComment(false)
                    .isSelected(false)
                    .build();
            commentRepository.save(comments);
        }
        return comments;
    }


    public List<Comment> allComment(long articlesId) {
        Optional<Article> targetArticle = articleRepository.findById(articlesId);
        if (targetArticle.isEmpty()) {
            throw new NullPointerException("게시글이 존재하지않습니다.");
        }
        List<Comment> resultBox = new ArrayList<>();
        List<Comment> result = commentRepository.getCommentList(articlesId);
        resultBox.addAll(result);
        return resultBox;
    }

    public boolean choose(Long articlesId, Long commentId) {
        Optional<Article> targetArticle = articleRepository.findById(articlesId);
        Optional<Comment> targetComment = commentRepository.findById(commentId);

//        게시글 존재여부 예외처리
        if (targetArticle.isEmpty()) {
            throw new NullPointerException("존재하지 않는 게시글입니다.");
        }
//        댓글 존재여부 예외처리
        if (targetComment.isEmpty()) {
            throw new NullPointerException("존재하지 않는 댓글입니다");
        }
//        동일 사용자 게시글 댓글 작성 예외처리
        if (targetArticle.get().getNickName().equals(targetComment.get().getNickName())) {
            throw new IllegalArgumentException("게시글 작성자와 댓글 작성자가 같습니다");
        }
        if (targetComment.get().getType().equals(Type.text)) {
            throw new IllegalArgumentException("금액 댓글만 채택이 가능합니다.");
        }
        targetComment.get().setSelected(true);
        targetArticle.get().setPrice(0);
        return true;
    }
}
