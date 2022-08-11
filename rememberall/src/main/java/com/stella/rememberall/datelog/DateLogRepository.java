package com.stella.rememberall.datelog;

import com.stella.rememberall.tripLog.TripLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DateLogRepository extends JpaRepository<DateLog, Long> {

    List<DateLog> findByTripLog(TripLog tripLog);
    List<DateLog> findByTripLogAndDate(TripLog tripLog, LocalDate date);
}