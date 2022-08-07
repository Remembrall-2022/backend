package com.stella.rememberall.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User saveEmailUser(EmailUserSaveRequestDto dto){
        return (User) userRepository.save(dto.toEntity());
    }

    @Transactional
    public User findEmailUser(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("no such data"));
    }

}
