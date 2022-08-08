package com.stella.rememberall.tripLog;

import com.stella.rememberall.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripLogRepository extends JpaRepository<TripLog, Long> {
    List<TripLog> findAllByUser(User user);
}
