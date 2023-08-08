package com.stella.rememberall.item;

import com.stella.rememberall.item.Item;
import com.stella.rememberall.user.domain.User;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "item_purchased_by_user")
@NoArgsConstructor
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

    @Builder
    public ItemPurchasedByUser(User user, Item item, int price) {
        this.user = user;
        this.item = item;
        this.price = price;
    }
}
