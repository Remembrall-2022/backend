package com.stella.rememberall.datelog.repository;

import com.stella.rememberall.datelog.domain.DateLog;
import com.stella.rememberall.tripLog.TripLog;
import com.stella.rememberall.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface DateLogRepository extends JpaRepository<DateLog, Long> {

    List<DateLog> findByTripLog(TripLog tripLog);
    List<DateLog> findByTripLogAndDate(TripLog tripLog, LocalDate date);
    @Query("select d from DateLog d where d.tripLog.user = :user and d.date = :date")
    List<DateLog> findAllByUserAndDate(User user, LocalDate date);
    boolean existsByTripLogAndDate(TripLog tripLog, LocalDate date);
}