package com.stella.rememberall.domain;

import javax.persistence.*;

@Table(name = "place")
@Entity
public class Place {
    @Id
    @Column(name = "place_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "place_name")
    private String name;

    @Column(name = "place_address")
    private String address;

}
