package com.stella.rememberall.user;

import com.stella.rememberall.security.JwtProvider;
import com.stella.rememberall.security.dto.TokenDto;
import com.stella.rememberall.security.dto.TokenRequestDto;
import com.stella.rememberall.security.exception.AuthErrorCode;
import com.stella.rememberall.security.exception.AuthException;
import com.stella.rememberall.user.domain.RefreshToken;
import com.stella.rememberall.user.domain.User;
import com.stella.rememberall.user.exception.MemberException;
import com.stella.rememberall.user.exception.MyErrorCode;
import com.stella.rememberall.user.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) throws MemberException, AuthException {

        String requestedAccessToken = tokenRequestDto.getAccessToken();
        String requestedRefreshToken = tokenRequestDto.getRefreshToken();

        User requestedUser = findUserById(getRequestedUserIdFromAccessToken(requestedAccessToken));
        RefreshToken foundRefreshToken = refreshTokenService.findRefreshTokenByUserOrElseThrows(requestedUser);
        checkRequestedRefreshTokenMatchesToFoundRefreshToken(requestedRefreshToken, foundRefreshToken);

        String accessToken = jwtProvider.createAccessToken(requestedUser.getId(), requestedUser.getRoles());
        String refreshToken = refreshTokenService.updateRefreshToken(requestedUser);

        return new TokenDto(accessToken, refreshToken);
    }

    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new MemberException(MyErrorCode.USER_NOT_FOUND));
    }

    private Long getRequestedUserIdFromAccessToken(String requestedAccessToken) {
        try {
            return Long.parseLong(jwtProvider.getAuthentication(requestedAccessToken).getName());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AuthException(AuthErrorCode.WRONG_TOKEN);
        }
    }

    private void checkRequestedRefreshTokenMatchesToFoundRefreshToken(String requestedRefreshToken, RefreshToken foundRefreshToken) {
        boolean isNotValid = true;
        try {
            isNotValid = !foundRefreshToken.getRefreshTokenValue().equals(jwtProvider.getRefreshTokenValue(requestedRefreshToken));
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

}
