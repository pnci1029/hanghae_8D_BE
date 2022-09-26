package com.example.checkcheck.model;

import com.example.checkcheck.model.articleModel.Article;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Member {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long memberId;

    @Column
    private String userName;

    @Column
    private String nickName;

    @Column
    private String password;

    @Column
    private String userEmail;

    @Column
    private String userRealEmail;

    @Column
    private int point= 0;

    @Column
    private String provider;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Article> article=new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Notification> notification = new ArrayList<>();

    @Column
    private Boolean isAccepted = false;

    @Column
    private Boolean isDeleted;

    @Builder
    public Member(String nickName, String password, String userEmail, String userName, Boolean isDeleted,
                  LocalDateTime createdAt, String provider, String userRealEmail, Boolean isAccepted) {
        this.nickName = nickName;
        this.userName = userName;
        this.password = password;
        this.userEmail = userEmail;
        this.provider = provider;
        this.userRealEmail = userRealEmail;
        this.isAccepted = isAccepted;
        this.isDeleted = isDeleted;

    }

    public void updatePoint(int newPoint) {
        point = newPoint;
    }

    public void updateNickName(String newNickName) {
        nickName = newNickName;
    }

    public void setEmailAgreement(){
        this.isAccepted = true;
    }

    public void setEmailOpposition(){
        this.isAccepted = false;
    }

    public void setMemberDelete() {
        isDeleted = true;
    }


}
