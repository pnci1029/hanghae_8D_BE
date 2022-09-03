package com.example.checkcheck.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class RefreshToken {

    @Id
    private String tokenKey;

    private String tokenValue;

    @Builder
    public RefreshToken(String key, String value) {
        this.tokenKey = key;
        this.tokenValue = value;
    }
}
