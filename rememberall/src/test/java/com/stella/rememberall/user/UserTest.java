package com.stella.rememberall.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class UserTest {

    @Autowired UserRepository userRepository;

    String email = "email..@com";

    @Test
    @DisplayName("멤버가 DB에 저장이 잘 되는지 확인")
    void saveUser(){
        // given
        User emailUser = createEmailUser();
        // when
        User savedEmailUser = userRepository.save(emailUser);
        // then
        assertThat(emailUser).isSameAs(savedEmailUser);
    }

    @Test
    @DisplayName("저장된 멤버가 제대로 조회되는지 확인")
    void findMember() {
        // given
        User emailUser = createEmailUser();
        User savedEmailUser = userRepository.save(emailUser);
        // when
        User findUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("no such data"));
        // then
        assertThat(findUser.getEmail()).isEqualTo(email);
    }

    User createEmailUser(){
//        String email = "email..@com";
        String password = "pw";
        String name = "name1";

        EmailUserSaveRequestDto dto = new EmailUserSaveRequestDto(email, password, name);
        User emailUser = dto.toEntity();
        return emailUser;
    }


}