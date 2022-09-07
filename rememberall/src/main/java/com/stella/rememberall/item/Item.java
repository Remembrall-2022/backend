package com.stella.rememberall.item;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "item")
@Entity
@Getter
public class Item {

    @Id
    @Column(name = "item_id")
    private Long id;

    @Column(name = "item_name")
    private String name;

    @Column(name = "item_price")
    private int price;

    @Column(name = "item_url")
    private String url;

    @OneToMany(mappedBy = "item")
    private List<ItemPurchasedByUser> itemPurchasedByUserList = new ArrayList<>();


}
