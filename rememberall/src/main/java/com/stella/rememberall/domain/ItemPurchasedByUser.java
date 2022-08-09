package com.stella.rememberall.domain;

import com.stella.rememberall.user.domain.User;

import javax.persistence.*;

@Entity
@Table(name = "item_purchased_by_user")
public class ItemPurchasedByUser {
    @Id
    @Column(name = "item_purchased_by_user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(name = "item_purchased_by_user_price")
    private int price;

}
