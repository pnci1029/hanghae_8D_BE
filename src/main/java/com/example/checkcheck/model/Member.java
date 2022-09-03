package com.example.checkcheck.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data @NoArgsConstructor
@Table(name = "member")
public class Member {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column
    private String nickName;

    @Column
    private String password;

    @Column
    private String userEmail;

    @Column
    String userRealEmail;

//    @Column
//    private String userRank;

    @Column
    private int point= 0;

    @Column
    private String provider;

    @Column
    private Double socialId;


    @Builder
    public Member(String nickName, String password, String userEmail,
                  LocalDateTime createdAt, String provider, Double socialId, String userRealEmail) {
        this.nickName = nickName;
        this.password = password;
        this.userEmail = userEmail;
        this.provider = provider;
        this.socialId = socialId;
        this.userRealEmail = userRealEmail;

    }

    public void updatePoint(int newPoint) {
        point = newPoint;
    }

}
