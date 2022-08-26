package com.stella.rememberall.dongdong;

import com.stella.rememberall.user.domain.User;
import com.stella.rememberall.user.dto.EmailUserSaveRequestDto;
import com.stella.rememberall.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class DongdongServiceTest {

    @Autowired UserRepository userRepository;
    @Autowired DongdongRepository dongdongRepository;
    @Autowired DongdongService dongdongService;

    @Test
    void 둥둥이조회() {
        // 유저 생성 후 저장
        String email = "stella@gmail.com";
        String password = "stellapw";
        String name = "stella1";
        EmailUserSaveRequestDto dto = new EmailUserSaveRequestDto(email, password, name);
        User user = dto.toEntity();
        userRepository.save(user);

        /**
         * User save 테스트
         * */
        User user1 = userRepository.findById(1L).get();
        Assertions.assertThat(user1.getId()).isEqualTo(1L);

        /** DongdongImgRepository 조회 테스트
        DongdongImg dongdongImg = dongdongImgRepository.findById(1L).get();
        Assertions.assertThat(dongdongImg.getId()).isEqualTo(1L);*/


        /** DongdongRepository 조회 테스트
        Dongdong dongdong1 = dongdongRepository.findById(user.getId()).get();
        Assertions.assertThat(dongdong1.getPoint()).isEqualTo(0L); */

    }
}