package com.stella.rememberall.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemRepository itemRepository;

    @GetMapping("/items")
    public List<Item> listItems() {

        return itemRepository.findAll();
    }

    @PostMapping("/item/{itemId}")
    public Long buyItem(@PathVariable Long itemId) {
        return 0L;
    }
}
