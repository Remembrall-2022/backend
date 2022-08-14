package com.stella.rememberall.user;

import com.stella.rememberall.common.exception.jpa.CommonJpaErrorCode;
import com.stella.rememberall.common.exception.jpa.CommonJpaException;
import com.stella.rememberall.user.emailAuth.EmailAuthService;
import com.stella.rememberall.common.redis.RedisUtil;
import com.stella.rememberall.security.JwtProvider;
import com.stella.rememberall.security.dto.TokenDto;
import com.stella.rememberall.security.dto.TokenRequestDto;
import com.stella.rememberall.security.exception.AuthErrorCode;
import com.stella.rememberall.security.exception.AuthException;
import com.stella.rememberall.user.domain.RefreshToken;
import com.stella.rememberall.user.domain.User;
import com.stella.rememberall.user.dto.EmailUserLoginRequestDto;
import com.stella.rememberall.user.dto.EmailUserSaveRequestDto;
import com.stella.rememberall.user.exception.MemberException;
import com.stella.rememberall.user.exception.MyErrorCode;
import com.stella.rememberall.user.repository.RefreshTokenRepository;
import com.stella.rememberall.user.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder pwdEncorder;
    private final JwtProvider jwtProvider;
    private final EmailAuthService emailService;
    private final RedisUtil redisUtil;

    @Transactional
    public void validateSignUpWithEmail(EmailUserSaveRequestDto saveRequestDto) throws MemberException {
        checkEmailDuplicate(saveRequestDto.getEmail());

        String redisKey = UUID.randomUUID().toString();
        emailService.sendSignUpAuthEmail(redisKey, saveRequestDto.getEmail());
        redisUtil.set(redisKey, saveRequestDto, 5);
    }

    private void checkEmailDuplicate(String email) {
        boolean isUserDuplicate = userRepository.existsByEmail(email);
        if(isUserDuplicate) throw new MemberException(MyErrorCode.DUPLICATED_REQUEST);
    }

    @Transactional
    public User findEmailUser(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MyErrorCode.ENTITY_NOT_FOUND));
    }

    @Transactional
    public TokenDto login(EmailUserLoginRequestDto requestDto) throws MemberException {
        User foundUser = findEmailUser(requestDto.getEmail());
        checkPassword(requestDto.getPassword(), foundUser.getPassword());

        String refreshTokenValue = createRefreshTokenValue();
        saveOrUpdateRefreshTokenValue(foundUser, refreshTokenValue); // TODO : 얘 id만 있으면 되니까 연관된거 고쳐

        return jwtProvider.createTokenDto(foundUser.getId(), foundUser.getRoles(), refreshTokenValue);
    }

    // 회원가입의 소셜 로그인
    @Transactional
    public TokenDto kakaoLoginInSignUp(User signUpSuccessUser) throws MemberException {
        String refreshTokenValue = createRefreshTokenValue();
        saveOrUpdateRefreshTokenValue(signUpSuccessUser, refreshTokenValue); // TODO : 얘 id만 있으면 되니까 연관된거 고쳐
        return jwtProvider.createTokenDto(signUpSuccessUser.getId(), signUpSuccessUser.getRoles(), refreshTokenValue);
    }

    // TODO : 소셜 로그인 : 인자는 토큰, kakaoId 찾기, 그담에 리프레시 토큰 발급



    private void saveOrUpdateRefreshTokenValue(User foundUser, String refreshTokenValue) throws MemberException {
        if(checkRefreshTokenExists(foundUser.getId()))
            updateRefreshTokenIfRefreshTokenValueExists(foundUser, refreshTokenValue);
        else
            saveRefreshTokenWithTokenValue(refreshTokenValue, foundUser.getId());
    }

    private boolean checkRefreshTokenExists(Long userPk) {
        return refreshTokenRepository.existsByUserPk(userPk);
    }

    private void updateRefreshTokenIfRefreshTokenValueExists(User foundUser, String refreshTokenValue) {
        if(refreshTokenRepository.existsByUserPk(foundUser.getId())) {
            RefreshToken foundRefreshToken = findRefreshTokenByUserOrElseThrows(foundUser);
            updateRefreshTokenWithNewRefreshTokenValue(foundRefreshToken, refreshTokenValue);
        }
    }

    private String createRefreshTokenValue() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private void saveRefreshTokenWithTokenValue(String refreshTokenValue, Long key) {
        RefreshToken refreshToken = RefreshToken.builder()
                .key(key)
                .refreshTokenValue(refreshTokenValue)
                .build();
        try {
            refreshTokenRepository.save(refreshToken);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommonJpaException(CommonJpaErrorCode.SAVE_FAIL);
        }
    }

    private void checkPassword(String requestPassword, String encodedOriginPassword) {
        boolean isNotCorrectPassword = !(pwdEncorder.matches(requestPassword, encodedOriginPassword));
        if(isNotCorrectPassword) throw new MemberException(MyErrorCode.WRONG_PASSWORD);
    }

    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto, HttpServletRequest httpServletRequest) throws MemberException, AuthException {

        String requestedAccessToken = tokenRequestDto.getAccessToken();
        String requestedRefreshToken = tokenRequestDto.getRefreshToken();

        User requestedUser = findUserById(getRequestedUserIdFromAccessToken(requestedAccessToken));
        RefreshToken foundRefreshToken = findRefreshTokenByUserOrElseThrows(requestedUser);
        checkRequestedRefreshTokenMatchesToFoundRefreshToken(requestedRefreshToken, foundRefreshToken);

        String newRefreshTokenValue = createRefreshTokenValue();
        TokenDto createdTokens = jwtProvider.createTokenDto(requestedUser.getId(), requestedUser.getRoles(), newRefreshTokenValue);
        updateRefreshTokenWithNewRefreshTokenValue(foundRefreshToken, newRefreshTokenValue);

        return createdTokens;
    }

    private void checkRequestedRefreshTokenMatchesToFoundRefreshToken(String requestedRefreshToken, RefreshToken foundRefreshToken) { //
        boolean isNotValid = false;
        try {
            isNotValid = foundRefreshToken.getRefreshTokenValue().equals(jwtProvider.getRefreshTokenValue(requestedRefreshToken));
        } catch (ExpiredJwtException e){
            log.error(e.getMessage());
            throw new AuthException(AuthErrorCode.EXPIRED_REFRESH_TOKEN);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AuthException(AuthErrorCode.WRONG_TOKEN);
        }
        if (isNotValid)
            throw new AuthException(AuthErrorCode.INVALID_REFRESH_TOKEN);
    }

    private RefreshToken findRefreshTokenByUserOrElseThrows(User requestedUser) {
        return refreshTokenRepository.findByUserPk(requestedUser.getId())
                .orElseThrow(() -> new AuthException(AuthErrorCode.UNSAVED_REFRESH_TOKEN));
    }

    private void updateRefreshTokenWithNewRefreshTokenValue(RefreshToken refreshToken, String refreshTokenValue) {
        RefreshToken updateRefreshToken = refreshToken.updateRefreshTokenValue(refreshTokenValue);
        try {
            refreshTokenRepository.save(updateRefreshToken);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommonJpaException(CommonJpaErrorCode.SAVE_FAIL);
        }
    }

    private long getRequestedUserIdFromAccessToken(String requestedAccessToken) { // 여기서
        try {
            return Long.parseLong(jwtProvider.getAuthentication(requestedAccessToken).getName());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AuthException(AuthErrorCode.WRONG_TOKEN);
        }
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new MemberException(MyErrorCode.ENTITY_NOT_FOUND));
    }

    @Transactional
    public void registerUser(String key) {
        EmailUserSaveRequestDto foundUserInRedis = checkUserExistsInRedis(key);
        saveUser(foundUserInRedis.toEntityWithEncodedPassword(pwdEncorder));
        deleteUserFromRedis(key);
    }

    private EmailUserSaveRequestDto checkUserExistsInRedis(String key) {
        EmailUserSaveRequestDto user = (EmailUserSaveRequestDto) redisUtil.get(key);
        if(user==null) throw new MemberException(MyErrorCode.ENTITY_NOT_FOUND_FROM_REDIS);
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

}
