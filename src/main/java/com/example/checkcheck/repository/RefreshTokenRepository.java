package com.example.checkcheck.repository;

import com.example.checkcheck.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    Optional<RefreshToken> findByTokenKey(String userEmail);

    Optional<RefreshToken> deleteByTokenKey(String userEmail);

}
