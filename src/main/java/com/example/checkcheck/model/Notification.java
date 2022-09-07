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
@Getter
@NoArgsConstructor
public class Notification extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "notification_id")
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    /**
     * 알림 message 클래스 Embedded*
     */
    @Embedded
    private NotificationMessage message;

    @Column(nullable = false)
    private Boolean readState;

    /**
     * 클릭 시 이동할 수 있는 link 필요
     */
    @Embedded
    private RedirectUrl url;

    /**
     * 멤버 변수이름 변경
     */
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "receiver_member_id")
    private Member receiver;

    @Builder
    public Notification(AlarmType alarmType, String message, Boolean readState, String url, Member receiver) {
        this.alarmType = alarmType;
        this.message = new NotificationMessage(message);
        this.readState = readState;
        this.url = new RedirectUrl(url);
        this.receiver = receiver;
    }

    public void changeState() {
        this.readState = true;
    }

    public String getMessage() {
        return message.getMessage();
    }

    public String getUrl() {
        return url.getUrl();
    }

}
