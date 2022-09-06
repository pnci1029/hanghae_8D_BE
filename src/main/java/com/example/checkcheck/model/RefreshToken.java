package com.example.checkcheck.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "RefreshToken")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long tokenId;


    @Column
    private String tokenKey;

    @Column
    private String tokenValue;

    @Builder
    public RefreshToken(String key, String value) {
        this.tokenKey = key;
        this.tokenValue = value;
    }
}
