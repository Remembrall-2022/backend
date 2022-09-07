package com.stella.rememberall.item;

import com.stella.rememberall.dongdong.DongdongService;
import com.stella.rememberall.user.domain.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Getter
public class ItemService {

    private final ItemRepository itemRepository;
    private final DongdongService dongdongService;
    private final ItemPurchasedByUserRepository itemPurchasedByUserRepository;

    @Transactional
    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    @Transactional
    public Long buyItem(Long itemId, User user) {
        Item item = itemRepository.findById(itemId).orElseThrow();
        dongdongService.payPoint(user, item.getId());
        itemPurchasedByUserRepository.save(ItemPurchasedByUser.builder()
                .item(item)
                .user(user)
                .price(item.getPrice())
                .build());
        // TODO: 유저별로 보유하고 있는 아이템 리스트 필요한지 고민
        // TODO: 아이템 아이디로 찾을 수 없는 경우 예외 처리
        return itemId;
    }
}
