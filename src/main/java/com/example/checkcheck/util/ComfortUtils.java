package com.example.checkcheck.util;


import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ComfortUtils {

    private Time time;

    public ComfortUtils(Time time) {
        this.time = time;
    }

    public Long getTime(LocalDateTime cratedAt) {
        long now = ChronoUnit.MINUTES.between(cratedAt, LocalDateTime.now());
        return now;
    }
}
