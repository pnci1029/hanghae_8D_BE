package com.example.checkcheck.repository;

import com.example.checkcheck.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUserRealEmail(String email);
    Optional<Member> findByUserEmail(String email);

    Optional<Member> findByNickName(String email);

}
