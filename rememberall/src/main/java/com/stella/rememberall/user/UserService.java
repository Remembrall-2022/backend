package com.stella.rememberall.user;

import com.stella.rememberall.common.response.OnlyResponseString;
import com.stella.rememberall.domain.AuthType;
import com.stella.rememberall.dongdong.DongdongRepository;
import com.stella.rememberall.security.JwtProvider;
import com.stella.rememberall.security.SecurityUtil;
import com.stella.rememberall.security.dto.TokenDto;
import com.stella.rememberall.security.dto.TokenRequestDto;
import com.stella.rememberall.security.exception.AuthErrorCode;
import com.stella.rememberall.security.exception.AuthException;
import com.stella.rememberall.tripLog.TripLog;
import com.stella.rememberall.tripLog.TripLogRepository;
import com.stella.rememberall.tripLog.TripLogService;
import com.stella.rememberall.user.domain.EmailAuth;
import com.stella.rememberall.user.domain.RefreshToken;
import com.stella.rememberall.user.domain.User;
import com.stella.rememberall.user.dto.UserInfoResponseDto;
import com.stella.rememberall.user.exception.MemberException;
import com.stella.rememberall.user.exception.MyErrorCode;
import com.stella.rememberall.user.repository.EmailAuthRepository;
import com.stella.rememberall.user.repository.RefreshTokenRepository;
import com.stella.rememberall.user.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final DongdongRepository dongdongRepository;
    private final EmailAuthRepository emailAuthRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TripLogService tripLogService;
    private final TripLogRepository tripLogRepository;

    public User getLoginedUser(){
        return SecurityUtil.getCurrentUserPk().flatMap(userRepository::findById)
                .orElseThrow(() -> new MemberException(MyErrorCode.USER_NOT_FOUND));
    }

    public TokenDto createTokenDtoAndUpdateRefreshTokenValue(User signUpSuccessUser) throws MemberException, AuthException {
        String accessToken = jwtProvider.createAccessToken(signUpSuccessUser.getId(), signUpSuccessUser.getRoles());
        String refreshToken = refreshTokenService.updateRefreshToken(signUpSuccessUser);
        return new TokenDto(accessToken, refreshToken);
    }

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

    @Transactional
    public UserInfoResponseDto getUserInfo(User loginedUser){
        return UserInfoResponseDto.of(loginedUser);
    }

    @Transactional
    public OnlyResponseString updateMyName(String newName, User loginedUser){
        loginedUser.updateName(newName);
        userRepository.save(loginedUser);
        return new OnlyResponseString("이름 수정에 성공했습니다.");
    }

    @Transactional
    public OnlyResponseString updateAlarmAgree(Boolean alarmAgree, User loginedUser){
        loginedUser.updateAlarmAgree(alarmAgree);
        userRepository.save(loginedUser);
        return new OnlyResponseString("알람 설정 수정에 성공했습니다.");
    }

    @Transactional
    public OnlyResponseString updateTermAgree(Boolean termAgree, User loginedUser){
        loginedUser.updateTermAgree(termAgree);
        userRepository.save(loginedUser);
        return new OnlyResponseString("약관 동의 설정 수정에 성공했습니다.");
    }

    @Transactional
    public void signout(User loginedUser) {

        // dongdong
        dongdongRepository.delete(loginedUser.getDongdong());

        // role
        userRepository.deleteRoleByUserUserId(loginedUser.getId());

        // email auth : 이메일 회원이었으면
        if(loginedUser.getAuthType()== AuthType.EMAIL){
            EmailAuth emailAuth = emailAuthRepository.findByEmail(loginedUser.getEmail())
                    .orElseThrow(() -> new MemberException(MyErrorCode.INVALID_REQUEST));
            emailAuthRepository.delete(emailAuth);
        }

        // refresh_token : 필수는 아님
        RefreshToken refreshToken = refreshTokenRepository.findByUserPk(loginedUser.getId())
                .orElseThrow(() -> new MemberException(MyErrorCode.INVALID_REQUEST));
        refreshTokenRepository.delete(refreshToken);

        // item_purchase_by_user


        // trip_log
        List<TripLog> tripLogList = tripLogRepository.findAllByUser(loginedUser);
        for(TripLog tripLog:tripLogList){
            tripLogService.deleteTripLog(tripLog.getId(), loginedUser);
        }

        userRepository.delete(loginedUser);

    }
}
