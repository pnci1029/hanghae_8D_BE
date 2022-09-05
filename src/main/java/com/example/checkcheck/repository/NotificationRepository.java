package com.example.checkcheck.repository;

import com.example.checkcheck.model.Member;
import com.example.checkcheck.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByMemberOrderByCreatedAtDesc(Member member);
    void deleteByMember(Member member);
}

