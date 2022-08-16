package com.stella.rememberall.user.repository;

import com.stella.rememberall.user.domain.EmailAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailAuthRepository extends JpaRepository<EmailAuth, Long> {
    Optional<EmailAuth> findByEmail(String email);
    boolean existsByEmail(String email);
}
