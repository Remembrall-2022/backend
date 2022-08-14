package com.stella.rememberall.user;

import com.stella.rememberall.common.exception.jpa.CommonJpaErrorCode;
import com.stella.rememberall.common.exception.jpa.CommonJpaException;
import com.stella.rememberall.user.emailAuth.EmailAuthService;
import com.stella.rememberall.common.redis.RedisUtil;
import com.stella.rememberall.security.JwtProvider;
import com.stella.rememberall.security.dto.TokenDto;
import com.stella.rememberall.user.domain.User;
import com.stella.rememberall.user.dto.EmailUserLoginRequestDto;
import com.stella.rememberall.user.dto.EmailUserSaveRequestDto;
import com.stella.rememberall.user.exception.MemberException;
import com.stella.rememberall.user.exception.MyErrorCode;
import com.stella.rememberall.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder pwdEncorder;
    private final JwtProvider jwtProvider;
    private final EmailAuthService emailService;
    private final RedisUtil redisUtil;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public void validateSignUpWithEmail(EmailUserSaveRequestDto saveRequestDto) throws MemberException {
        checkEmailDuplicate(saveRequestDto.getEmail());
        String redisKey = UUID.randomUUID().toString();
        emailService.sendSignUpAuthEmail(redisKey, saveRequestDto.getEmail());
        redisUtil.set(redisKey, saveRequestDto, 5);
    }

    private void checkEmailDuplicate(String email) {
        boolean isUserDuplicate = userRepository.existsByEmail(email);
        if(isUserDuplicate) throw new MemberException(MyErrorCode.DUPLICATED_EMAIL);
    }

    @Transactional
    public void registerUser(String key) {
        EmailUserSaveRequestDto foundUserInRedis = checkUserExistsInRedis(key);
        saveUser(foundUserInRedis.toEntityWithEncodedPassword(pwdEncorder));
        deleteUserFromRedis(key);
    }

    private EmailUserSaveRequestDto checkUserExistsInRedis(String key) {
        EmailUserSaveRequestDto user = (EmailUserSaveRequestDto) redisUtil.get(key);
        if(user==null) throw new MemberException(MyErrorCode.USER_NOT_FOUND_FROM_REDIS);
        return user;
    }

    private User saveUser(User foundUserInRedis) {
        try {
            return userRepository.save(foundUserInRedis);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommonJpaException(CommonJpaErrorCode.SAVE_FAIL);
        }
    }

    private void deleteUserFromRedis(String key) {
        redisUtil.delete(key);
    }

    @Transactional
    public TokenDto login(EmailUserLoginRequestDto requestDto) throws MemberException {
        User foundUser = findEmailUser(requestDto.getEmail());
        checkPassword(requestDto.getPassword(), foundUser.getPassword());
        return createTokenDtoAndUpdateRefreshTokenValue(foundUser);
    }

    public User findEmailUser(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MyErrorCode.USER_NOT_FOUND));
    }

    private void checkPassword(String requestPassword, String encodedOriginPassword) {
        boolean isNotCorrectPassword = !(pwdEncorder.matches(requestPassword, encodedOriginPassword));
        if(isNotCorrectPassword) throw new MemberException(MyErrorCode.WRONG_PASSWORD);
    }

    @Transactional
    public TokenDto kakaoLoginInSignUp(User signUpSuccessUser) throws MemberException {
        return createTokenDtoAndUpdateRefreshTokenValue(signUpSuccessUser);
    }

    private TokenDto createTokenDtoAndUpdateRefreshTokenValue(User signUpSuccessUser) {
        String accessToken = jwtProvider.createAccessToken(signUpSuccessUser.getId(), signUpSuccessUser.getRoles());
        String refreshToken = refreshTokenService.updateRefreshToken(signUpSuccessUser);
        return new TokenDto(accessToken, refreshToken);
    }

    // TODO : 소셜 로그인 : 인자는 토큰, kakaoId 찾기, 그담에 리프레시 토큰 발급





}
