package com.stella.rememberall.placelog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Table(name = "place")
@Entity
public class Place {
    @Id
    @Column(name = "place_id")
    private Long id;

    @Column(name = "place_name")
    private String name;

    @Column(name = "place_address")
    private String address;

    private Double longitude;

    private Double latitude;

    @Builder
    public Place(Long id, String name, String address, Double longitude, Double latitude){
        this.id = id;
        this.name = name;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
    }

}
