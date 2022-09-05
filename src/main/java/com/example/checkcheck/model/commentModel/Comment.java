package com.example.checkcheck.model.commentModel;

import com.example.checkcheck.model.Member;
import com.example.checkcheck.model.articleModel.Article;
import com.example.checkcheck.util.TimeStamped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Comment extends TimeStamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "comment_id")
    private Long commentId;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Type type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
