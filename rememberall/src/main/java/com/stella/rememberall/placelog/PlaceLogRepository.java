package com.stella.rememberall.placelog;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceLogRepository extends JpaRepository<PlaceLog, Long> {
    boolean existsByPlace(Place place);
    List<PlaceLog> findAllByPlace(Place place);
}
