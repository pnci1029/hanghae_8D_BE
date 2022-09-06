package com.example.checkcheck.util;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@MappedSuperclass // Entity가 자동으로 컬럼으로 인식합니다.
@EntityListeners(AuditingEntityListener.class)
public class TimeStamped {

    @CreatedDate // 생성시간
    @JsonFormat(pattern = "yyyy.MM.dd HH:mm")
    private LocalDateTime createdAt = LocalDateTime.now(ZoneId.of("Asia/Seoul"));;
}