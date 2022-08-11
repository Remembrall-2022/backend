package com.stella.rememberall.dongdong;

import com.stella.rememberall.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DongdongService {

    DongdongRepository dongdongRepository;

    Dongdong readDongdong(User user) {
        return dongdongRepository.findById(user.getId()).get();
    }
}
