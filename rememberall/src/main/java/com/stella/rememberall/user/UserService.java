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
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

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
        User user = dto.toEntityWithEncodedPassword(pwdEncorder);
        // TODO : 동동이 캐릭터 생성

        return userRepository.save(user).getId();
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
// TODO : email 인증 체크        if(isNotAuthedEmail) throw new MemberException("");
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
        TokenDto tokenDto = jwtProvider.createTokenDto(foundUser.getId(), foundUser.getRoles());

        // 리프레시 토큰 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(foundUser.getId())
                .token(tokenDto.getRefreshToken())
                .build();
        refreshTokenRepository.save(refreshToken);

        return tokenDto;
    }

    private void checkPassword(String requestPassword, String encodedOriginPassword) {
        String encodedRequestPassword = pwdEncorder.encode(requestPassword);
        boolean isNotCorrectPassword = !(pwdEncorder.matches(requestPassword, encodedOriginPassword));
        if(isNotCorrectPassword) throw new MemberException(MyErrorCode.WRONG_PASSWORD);
    }

    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto, HttpServletRequest httpServletRequest) throws MemberException, AuthException {
        // 만료된 refresh token 에러
        jwtProvider.validationToken(tokenRequestDto.getRefreshToken(), httpServletRequest);
//        if (!jwtProvider.validationToken(tokenRequestDto.getRefreshToken(), httpServletRequest)) {
//            throw new AuthException(AuthErrorCode.EXPIRED_REFRESH_TOKEN);
//        }
        // AccessToken 에서 Username (pk) 가져오기
        String accessToken = tokenRequestDto.getAccessToken();
        Authentication authentication = jwtProvider.getAuthentication(accessToken);

        // user pk로 유저 검색
        User user = userRepository.findById(Long.parseLong(authentication.getName()))
                .orElseThrow(() -> new MemberException(MyErrorCode.ENTITY_NOT_FOUND));
        // repo 에 저장된 Refresh Token이 없음
        RefreshToken refreshToken = refreshTokenRepository.findByUserPk(user.getId())
                .orElseThrow(() -> new AuthException(AuthErrorCode.UNSAVED_REFRESH_TOKEN));

        // 리프레시 토큰 불일치 에러
        if (!refreshToken.getToken().equals(tokenRequestDto.getRefreshToken()))
            throw new AuthException(AuthErrorCode.INVALID_REFRESH_TOKEN);

        // AccessToken, RefreshToken 토큰 재발급, 리프레쉬 토큰 저장
        TokenDto newCreatedToken = jwtProvider.createTokenDto(user.getId(), user.getRoles());
        RefreshToken updateRefreshToken = refreshToken.updateToken(newCreatedToken.getRefreshToken());
        refreshTokenRepository.save(updateRefreshToken);

        return newCreatedToken;

    }
}
