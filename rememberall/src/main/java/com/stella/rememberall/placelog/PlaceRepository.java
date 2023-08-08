package com.stella.rememberall.placelog;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    Optional<Place> findByName(String placeName);
    void deleteByName(String placeName);

}
