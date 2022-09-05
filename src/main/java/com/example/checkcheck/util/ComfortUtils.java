package com.example.checkcheck.util;


import org.springframework.stereotype.Component;

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


}
