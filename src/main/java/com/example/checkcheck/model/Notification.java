package com.example.checkcheck.model;

import com.example.checkcheck.util.RedirectUrl;
import com.example.checkcheck.util.TimeStamped;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

/**
 * 알림 Entity
 */

@Entity
@Getter@Setter
@NoArgsConstructor
public class Notification extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    /**
     * 알림 message
     */
    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private Boolean readState;

    /**
     * 클릭 시 이동할 수 있는 link 필요
     */

    @Column(nullable = false)
    private String url;

    /**
     * 멤버 변수이름 변경
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "receiver_member_id")
    private Member receiver;

    private String title;

//    private LocalDateTime createdAt;

    @Builder
    public Notification(AlarmType alarmType, String message, Boolean readState,
                        Long articlesId, Member receiver, String title) {
        this.alarmType = alarmType;
        this.message = message;
        this.readState = readState;
        this.articlesId = articlesId;
        this.receiver = receiver;
        this.title = title;
//        this.createdAt = createdAt;
    }

    public void changeState() {
        readState = true;
    }


}
