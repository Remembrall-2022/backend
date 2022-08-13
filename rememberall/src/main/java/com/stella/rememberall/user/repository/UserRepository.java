package com.stella.rememberall.user.repository;

import com.stella.rememberall.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUniqueValue(String email);
    boolean existsByUniqueValue(String email);
}
