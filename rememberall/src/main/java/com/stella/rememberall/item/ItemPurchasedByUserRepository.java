package com.stella.rememberall.item;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemPurchasedByUserRepository extends JpaRepository<ItemPurchasedByUser, Long> {
}
