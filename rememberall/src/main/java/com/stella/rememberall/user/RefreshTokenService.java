package com.stella.rememberall.user;

import com.stella.rememberall.common.exception.jpa.CommonJpaErrorCode;
import com.stella.rememberall.common.exception.jpa.CommonJpaException;
import com.stella.rememberall.security.JwtProvider;
import com.stella.rememberall.security.exception.AuthErrorCode;
import com.stella.rememberall.security.exception.AuthException;
import com.stella.rememberall.user.domain.RefreshToken;
import com.stella.rememberall.user.domain.User;
import com.stella.rememberall.user.exception.MemberException;
import com.stella.rememberall.user.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class RefreshTokenService {

    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public String updateRefreshToken(User user) throws MemberException, AuthException{
        String refreshTokenValue = createRefreshTokenValue();
        String refreshToken = jwtProvider.createRefreshToken(refreshTokenValue);
        saveOrUpdateRefreshTokenValue(user, refreshTokenValue);
        return refreshToken;
    }

    private String createRefreshTokenValue() {
        return UUID.randomUUID().toString().replace("-", "");
    }

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

    public RefreshToken findRefreshTokenByUserOrElseThrows(User requestedUser) {
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
}
