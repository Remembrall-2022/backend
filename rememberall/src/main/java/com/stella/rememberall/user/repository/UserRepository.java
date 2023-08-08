package com.stella.rememberall.user.repository;

import com.stella.rememberall.domain.AuthType;
import com.stella.rememberall.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User>  findByKakaoId(Long kakaoId);
    boolean existsByEmail(String email);
    boolean existsByKakaoId(Long kakaoId);
    boolean existsByEmailAndAuthType(String email, AuthType authType);
    boolean existsByKakaoIdAndAuthType(Long kakaoId, AuthType authType);

    @Modifying
    @Query(value = "delete from user_roles where user_user_id=?1", nativeQuery = true)
    void deleteRoleByUserUserId(Long id);
}
