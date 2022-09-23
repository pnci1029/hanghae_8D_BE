package com.example.checkcheck.repository;

import com.example.checkcheck.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, CrudRepository<Member, Long> {

    Optional<Member> findByUserRealEmail(String email);
    Optional<Member> findByUserEmail(String email);

    Optional<Member> findByNickName(String email);

    Optional<Member> findByIsAccepted(Boolean isAccepted);
    Optional<Member> findByUserName(String nickName);
}
