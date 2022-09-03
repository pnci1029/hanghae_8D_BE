package com.example.checkcheck.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Builder @Data
@AllArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    @Column
    private String image;

    @Column
    @JsonIgnore
    private String userEmail;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name="ARTICLE_ID")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Article article;

}
