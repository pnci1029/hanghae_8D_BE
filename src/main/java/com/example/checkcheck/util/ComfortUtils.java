package com.example.checkcheck.util;


import com.example.checkcheck.model.articleModel.Category;
import com.example.checkcheck.model.articleModel.Process;
import com.example.checkcheck.repository.NotificationRepository;
import com.example.checkcheck.security.UserDetailsImpl;
import com.example.checkcheck.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class ComfortUtils {

    private Time time;

    public ComfortUtils(Time time) {
        this.time = time;
    }

    public String getTime(LocalDateTime cratedAt) {
        long now = ChronoUnit.MINUTES.between(cratedAt , LocalDateTime.now());

        return time.times(now);
    }

    public String getUserRank(int point) {

        if (point >= 0 && point < 101) {
            return "B";
        } else if (point >= 101 && point < 201) {
            return "S";
        } else if (point >= 201 && point < 501) {
            return "G";
        } else if (point >= 501 && point < 1001) {
            return "P";
        } else
            return "D";
    }

    public String getCategoryKorean(Category category) {
        if (category.equals(Category.clothes)) {
            return "의류/잡화";
        } else if (category.equals(Category.digital)) {
            return "디지털/생활가전";
        } else if (category.equals(Category.sports)) {
            return "스포츠/레저";
        } else if (category.equals(Category.interior)) {
            return "가구/인테리어";
        } else if (category.equals(Category.hobby)) {
            return "도서/여행/취미";
        } else if (category.equals(Category.food)) {
            return "식품";
        } else if (category.equals(Category.pet)) {
            return "반려동물, 식물";
        } else
            return "기타";
    }

    public String getProcessKorean(Process process) {
        if (process.equals(Process.process)) {
            return "진행중";
        } else if (process.equals(Process.done)) {
            return "채택 성공";
        } else
            return String.valueOf(process);
    }




}
