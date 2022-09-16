package com.example.checkcheck.model.commentModel;

import com.example.checkcheck.model.Member;
import com.example.checkcheck.model.articleModel.Article;
import com.example.checkcheck.util.TimeStamped;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Comment extends TimeStamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "comment_id")
    private Long commentId;

    @Size(max = 80)
    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private String nickName;

    @Column(nullable = false)
    private String userRank;


    @Column(nullable = false)
    private boolean isMyComment;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Type type;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @Column
    private boolean isSelected;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;















    public void chooseComment(boolean selectedComment) {
        isSelected = selectedComment;
    }

}
