package com.stella.rememberall.item;

import com.stella.rememberall.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items")
    public List<Item> listItems() {

        return itemService.findItems();
    }

    @PostMapping("/item/{itemId}")
    public Long buyItem(@PathVariable Long itemId, @AuthenticationPrincipal User user) {
        return itemService.buyItem(itemId, user);
    }
}
