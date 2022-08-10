package com.stella.rememberall.user;

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

    @Transactional
    public Long saveEmailUser(EmailUserSaveRequestDto dto) throws MemberException {
        // TODO : 이메일 인증
        checkEmailValid(dto.getEmail());
        User usertoSave = dto.toEntityWithEncodedPassword(pwdEncorder);
        // TODO : 동동이 캐릭터 생성

        return userRepository.save(usertoSave).getId(); // TODO : save 공통예외 정해지면 수정하기
    }

    private void checkEmailValid(String email) throws MemberException {
        checkEmailDuplicate(email);
        checkEmailAuthed(email);
    }

    private void checkEmailDuplicate(String email) {
        boolean isUserDuplicate = userRepository.existsByEmail(email);
        if(isUserDuplicate) throw new MemberException(MyErrorCode.DUPLICATED_REQUEST);
    }

    private void checkEmailAuthed(String email) {
// TODO : email 인증 체크
//  if(isNotAuthedEmail) throw new MemberException("");
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
        TokenDto createdTokens = jwtProvider.createTokenDto(foundUser.getId(), foundUser.getRoles(), refreshTokenValue);
        saveRefreshTokenWithTokenValue(refreshTokenValue, foundUser.getId());

        return createdTokens;
    }

    private String createRefreshTokenValue() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private void saveRefreshTokenWithTokenValue(String refreshTokenValue, Long key) {
        RefreshToken refreshToken = RefreshToken.builder()
                .key(key)
                .refreshTokenValue(refreshTokenValue)
                .build();
        refreshTokenRepository.save(refreshToken);
    }

    private void checkPassword(String requestPassword, String encodedOriginPassword) {
        String encodedRequestPassword = pwdEncorder.encode(requestPassword);
        boolean isNotCorrectPassword = !(pwdEncorder.matches(requestPassword, encodedOriginPassword));
        if(isNotCorrectPassword) throw new MemberException(MyErrorCode.WRONG_PASSWORD);
    }

    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto, HttpServletRequest httpServletRequest) throws MemberException, AuthException {

        String requestedAccessToken = tokenRequestDto.getAccessToken();
        String requestedRefreshToken = tokenRequestDto.getRefreshToken();

        jwtProvider.validationToken(requestedAccessToken, httpServletRequest);
        User requestedUser = findUserById(getRequestedUserIdFromAccessToken(requestedAccessToken));

        RefreshToken foundRefreshToken = findRefreshTokenByUserOrElseThrows(requestedUser);
        checkRequestedRefreshTokenMatchesToFoundRefreshToken(requestedRefreshToken, foundRefreshToken);

        String newRefreshTokenValue = createRefreshTokenValue();
        TokenDto createdTokens = jwtProvider.createTokenDto(requestedUser.getId(), requestedUser.getRoles(), newRefreshTokenValue);
        updateRefreshTokenWithNewRefreshTokenValue(foundRefreshToken, newRefreshTokenValue);

        return createdTokens;
    }

    private void checkRequestedRefreshTokenMatchesToFoundRefreshToken(String requestedRefreshToken, RefreshToken foundRefreshToken) {
        if (!foundRefreshToken.getRefreshTokenValue().equals(jwtProvider.getRefreshTokenValue(requestedRefreshToken)))
            throw new AuthException(AuthErrorCode.INVALID_REFRESH_TOKEN);
    }

    private RefreshToken findRefreshTokenByUserOrElseThrows(User requestedUser) {
        return refreshTokenRepository.findByUserPk(requestedUser.getId())
                .orElseThrow(() -> new AuthException(AuthErrorCode.UNSAVED_REFRESH_TOKEN));
    }

    private void updateRefreshTokenWithNewRefreshTokenValue(RefreshToken refreshToken, String refreshTokenValue) {
        RefreshToken updateRefreshToken = refreshToken.updateRefreshTokenValue(refreshTokenValue);
        refreshTokenRepository.save(updateRefreshToken);
    }

    private long getRequestedUserIdFromAccessToken(String requestedAccessToken) {
        return Long.parseLong(jwtProvider.getAuthentication(requestedAccessToken).getName());
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new MemberException(MyErrorCode.ENTITY_NOT_FOUND));
    }
}
