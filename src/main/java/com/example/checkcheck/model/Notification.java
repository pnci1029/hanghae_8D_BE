package com.example.checkcheck.model;

import com.example.checkcheck.dto.requestDto.NotificationRequestDto;
import com.example.checkcheck.util.TimeStamped;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 알림 Entity
 */

@Entity
@Getter
@NoArgsConstructor
public class Notification extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    @Column
    private String message;

    @Column(nullable = false)
    private boolean readState;

    @ManyToOne
    @JoinColumn
    private Member member;

    public Notification(NotificationRequestDto requestDto, Member member) {
        this.alarmType = requestDto.getAlarmType();
        this.message = requestDto.getMessage();
        this.readState = false;
        this.member = member;
    }

    public void changeState() {
        this.readState = true;
    }

}
