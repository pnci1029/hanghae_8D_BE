package com.example.checkcheck.repository;

import com.example.checkcheck.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository <Notification, Long> {

    @Query(value = "select n from Notification n where n.receiver.memberId = :memberId order by n.id desc")
    List<Notification> findAllByReceiver_MemberId(@Param("memberId")Long memberId);

    @Query(value = "select count(n) from Notification n where n.receiver.memberId =:memberId and n.readState = false")
    Long countUnReadStateNotifications(@Param("memberId") Long memberId);


    void deleteAllByReceiver_MemberId(Long receiverId);


}

