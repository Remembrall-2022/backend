package com.stella.rememberall.user.repository;

import com.stella.rememberall.user.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUserPk(Long id);
    boolean existsByUserPk(Long id);
}
