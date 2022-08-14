package com.stella.rememberall.user.kakao;

import com.stella.rememberall.security.dto.TokenDto;
import com.stella.rememberall.user.UserService;
import com.stella.rememberall.user.domain.User;
import com.stella.rememberall.user.exception.MemberException;
import com.stella.rememberall.user.exception.MyErrorCode;
import com.stella.rememberall.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoSignService {

    private final UserService userService;
    private final UserRepository userRepository;

    @Transactional
    public TokenDto socialSignup(KakaoProfile kakaoProfile) throws MemberException {
        checkKakaoProfileNull(kakaoProfile);
        checkKakaoProfileEmailNullThenUnlink(kakaoProfile);

        String email = kakaoProfile.getKakao_account().getEmail();
        Long id = kakaoProfile.getId();
        String name = kakaoProfile.getProperties().getNickname();

        checkEmailDuplicate(email);
        checkKakaoDuplicate(id);

        KakaoUserSaveRequestDto saveRequestDto = new KakaoUserSaveRequestDto(email, id, name);
        User savedUser = userRepository.save(saveRequestDto.toEntity());
        return userService.kakaoLoginInSignUp(savedUser);
    }

    private void checkKakaoProfileNull(KakaoProfile kakaoProfile) {
        if(kakaoProfile == null) throw new KakaoException(KakaoErrorCode.INVALID_TOKEN);
    }

    private void checkKakaoProfileEmailNullThenUnlink(KakaoProfile kakaoProfile) {
        if(kakaoProfile.getKakao_account().getEmail() == null){
//            kakaoService.kakaoUnlink()
        }
    }

    private void checkEmailDuplicate(String email){
        if(userRepository.existsByEmail(email))
            throw new MemberException(MyErrorCode.DUPLICATED_REQUEST); // 이미 이메일로 가입한 회원입니다.
    }

    private void checkKakaoDuplicate(Long kakaoId){
        if(userRepository.existsByKakaoId(kakaoId))
            throw new MemberException(MyErrorCode.DUPLICATED_REQUEST); // 이미 카카오로 가입한 회원입니다.
    }



}