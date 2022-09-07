package com.example.checkcheck.util;

import com.example.checkcheck.exception.CustomException;
import com.example.checkcheck.exception.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor
public class RedirectUrl {
    private static final int MAX_LENGTH = 255;

    @Column(nullable = false)
    private String url;

    public RedirectUrl(String url) {
        if (isNotValidRelatedURL(url)) {
            throw new CustomException(ErrorCode.NOT_VALIDURL);
        }
        this.url = url;
    }

    private boolean isNotValidRelatedURL(String url) {
        return Objects.isNull(url) || url.length() > MAX_LENGTH || url.isEmpty();

    }
}
