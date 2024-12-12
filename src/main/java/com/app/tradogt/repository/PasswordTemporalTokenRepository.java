package com.app.tradogt.repository;

import com.app.tradogt.entity.PasswordTemporalToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordTemporalTokenRepository extends JpaRepository<PasswordTemporalToken, Integer> {
    Optional<PasswordTemporalToken> findByTokenPass(String token);
    Optional<PasswordTemporalToken> findByEmail(String email);

    void deleteByTokenPass(String tokenPass);
}
