package com.example.checkcheck.model.articleModel;

import com.example.checkcheck.model.Image;
import com.example.checkcheck.model.Member;
import com.example.checkcheck.model.commentModel.Comment;
import com.example.checkcheck.util.TimeStamped;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Article extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleId;

    @Column(nullable = false)
    private String nickName;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private String userRank;

    @Column
    private int selectedPrice;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Image> images;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Comment> comment=new ArrayList<>();
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Process process;

    @Column
    private String userEmail;

    @Column
    private int articleCount;

    @Builder
    public Article(String nickName, String title, String content, Category category, String userEmail,
                   int price, int selectedPrice, List<Image> images, Process process, String userRank,
                   Member member) {
        this.nickName = nickName;
        this.title = title;
        this.content = content;
        this.price = price;
        this.selectedPrice = selectedPrice;
        this.images = images;
        this.category = category;
        this.process = process;
        this.userRank = userRank;
        this.member = member;
        this.userEmail = userEmail;
    }




    public void updateProcess(Process newProcess) {
        process = newProcess;
    }

    public void choosePrice(int comment) {
        selectedPrice = comment;
    }


}