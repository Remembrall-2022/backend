package com.stella.rememberall.user;

import com.stella.rememberall.common.exception.jpa.CommonJpaErrorCode;
import com.stella.rememberall.common.exception.jpa.CommonJpaException;
import com.stella.rememberall.common.response.OnlyResponseString;
import com.stella.rememberall.dongdong.DongdongService;
import com.stella.rememberall.user.domain.EmailAuth;
import com.stella.rememberall.user.emailAuth.EmailAuthService;
//import com.stella.rememberall.common.redis.RedisUtil;
import com.stella.rememberall.security.dto.TokenDto;
import com.stella.rememberall.user.domain.User;
import com.stella.rememberall.user.dto.EmailUserLoginRequestDto;
import com.stella.rememberall.user.dto.EmailUserSaveRequestDto;
import com.stella.rememberall.user.emailAuth.dto.EmailUserAuthRequestDto;
import com.stella.rememberall.user.emailAuth.dto.EmailUserPasswordUpdateRequestDto;
import com.stella.rememberall.user.emailAuth.dto.EmailUserValidRequestDto;
import com.stella.rememberall.user.exception.MemberException;
import com.stella.rememberall.user.exception.MyErrorCode;
import com.stella.rememberall.user.repository.EmailAuthRepository;
import com.stella.rememberall.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailUserService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final EmailAuthService emailAuthService;
    private final EmailAuthRepository emailAuthRepository;
    private final PasswordEncoder pwdEncorder;
//    private final RedisUtil redisUtil;
    private final DongdongService dongdongService;

    @Transactional
    public void sendAuthCode(EmailUserAuthRequestDto requestDto) {
        String authCode = createCode();
        saveOrUpdateEmailAuth(requestDto, authCode);
        emailAuthService.sendAuthCodeEmail(authCode, requestDto.getEmail());
    }

    @Transactional
    public void validateSignUpWithEmail(EmailUserSaveRequestDto saveRequestDto) throws MemberException {
        checkEmailDuplicate(saveRequestDto.getEmail());
        String redisKey = UUID.randomUUID().toString();
        emailAuthService.sendSignUpAuthEmail(redisKey, saveRequestDto.getEmail());
//        redisUtil.set(redisKey, saveRequestDto, 5);
    }

    private void checkEmailDuplicate(String email) {
        boolean isUserDuplicate = userRepository.existsByEmail(email);
        if(isUserDuplicate) throw new MemberException(MyErrorCode.DUPLICATED_EMAIL);
    }

    @Transactional
    public void registerUser(EmailUserSaveRequestDto requestDto) {
        checkEmailDuplicate(requestDto.getEmail());
        checkEmailAuthed(requestDto.getEmail());
        User user = requestDto.toEntityWithEncodedPassword(pwdEncorder);
        saveUser(user);
        dongdongService.createDongdong(user);
    }

//    @Transactional
//    public void registerUser(String key) {
//        EmailUserSaveRequestDto foundUserInRedis = checkUserExistsInRedis(key);
//        User savedUser = foundUserInRedis.toEntityWithEncodedPassword(pwdEncorder);
//        saveUser(savedUser);
//        dongdongService.createDongdong(savedUser);
//        deleteUserFromRedis(key);
//    }
//
//    private EmailUserSaveRequestDto checkUserExistsInRedis(String key) {
//        EmailUserSaveRequestDto user = (EmailUserSaveRequestDto) redisUtil.get(key);
//        if(user==null) throw new MemberException(MyErrorCode.USER_NOT_FOUND_FROM_REDIS);
//        return user;
//    }

    private User saveUser(User foundUserInRedis) {
        try {
            return userRepository.save(foundUserInRedis);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommonJpaException(CommonJpaErrorCode.SAVE_FAIL);
        }
    }

//    private void deleteUserFromRedis(String key) {
//        redisUtil.delete(key);
//    }

    @Transactional
    public TokenDto login(EmailUserLoginRequestDto requestDto) throws MemberException {
        User foundUser = findEmailUser(requestDto.getEmail());
        checkPassword(requestDto.getPassword(), foundUser.getPassword());
        return userService.createTokenDtoAndUpdateRefreshTokenValue(foundUser);
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
    public void requestEmailValidation(EmailUserAuthRequestDto requestDto) throws MemberException{
        checkEmailUserExists(requestDto.getEmail());
        String authCode = createCode();
        saveOrUpdateEmailAuth(requestDto, authCode);
        emailAuthService.sendAuthCodeEmail(authCode, requestDto.getEmail());
    }

    private void checkEmailUserExists(String requestedEmail) {
        if(!userRepository.existsByEmail(requestedEmail)) throw new MemberException(MyErrorCode.USER_NOT_FOUND);
    }

    private String createCode() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 6; i++) { // 인증코드 6자리
            key.append((rnd.nextInt(10)));
        }
        return key.toString();
    }

    private void saveOrUpdateEmailAuth(EmailUserAuthRequestDto requestDto, String authCode) {
        if(isEmailAuthExists(requestDto.getEmail()))
            updateEmailAuth(requestDto, authCode);
        else
            saveEmailAuth(requestDto.getEmail(), authCode);
    }

    private boolean isEmailAuthExists(String email) {
        return emailAuthRepository.existsByEmail(email);
    }

    private void updateEmailAuth(EmailUserAuthRequestDto requestDto, String authCode) {
        EmailAuth emailAuth = emailAuthRepository.findByEmail(requestDto.getEmail()).get();
        emailAuth.updateAuthed(false);
        emailAuth.updateAuthCode(authCode);
        emailAuth.updateAuthValidTime(LocalDateTime.now().plusMinutes(5));
        emailAuthRepository.save(emailAuth);
    }

    private void saveEmailAuth(String email, String authCode) {
        EmailAuth emailAuth = EmailAuth.builder()
                .authCode(authCode)
                .email(email)
                .authValidTime(LocalDateTime.now().plusMinutes(5))
                .authed(false)
                .build();
        emailAuthRepository.save(emailAuth);
    }

    @Transactional
    public OnlyResponseString validEmail(EmailUserValidRequestDto requestDto) throws MemberException{
        String requestedEmail = requestDto.getEmail();
        String requestedAuthCode = requestDto.getAuthCode();

        EmailAuth emailAuth = getEmailAuth(requestedEmail);
        updateAuthedIfAuthCodeIsCorrect(emailAuth, requestedAuthCode);
        return new OnlyResponseString("이메일 인증에 성공했습니다.");
    }

    private EmailAuth getEmailAuth(String requestedEmail) {
        return emailAuthRepository.findByEmail(requestedEmail)
                .orElseThrow(() -> new MemberException(MyErrorCode.INVALID_REQUEST, "이메일 인증을 요청한 적 없는 회원입니다."));
    }

    private void updateAuthedIfAuthCodeIsCorrect(EmailAuth emailAuth, String requestedAuthCode) throws MemberException {
        String realCode = emailAuth.getAuthCode();
        checkEmailAuthCodeCorrect(realCode, requestedAuthCode);
        checkEmailAuthCodeValid(emailAuth.getAuthValidTime());
        emailAuth.updateAuthed(true);
    }

    private void checkEmailAuthCodeCorrect(String realCode, String requestedAuthCode) {
        if(!realCode.equals(requestedAuthCode))
            throw new MemberException(MyErrorCode.WRONG_AUTH_CODE);
    }

    private void checkEmailAuthCodeValid(LocalDateTime authValidTime) {
        if(authValidTime.isBefore(LocalDateTime.now()))
            throw new MemberException(MyErrorCode.TIMEOUT_AUTH_REQUEST);
    }

    @Transactional
    public OnlyResponseString updatePasswordIfEmailAuthed(EmailUserPasswordUpdateRequestDto requestDto){
        String requestedEmail = requestDto.getEmail();
        checkEmailUserExists(requestedEmail);
        checkEmailAuthed(requestedEmail);
        User requestUser = findEmailUser(requestedEmail);
        String encodedNewPassword = pwdEncorder.encode(requestDto.getNewPassword());
        requestUser.updatePassword(encodedNewPassword);
        return new OnlyResponseString("비밀번호 변경에 성공했습니다.");
    }

    private void checkEmailAuthed(String email){
        EmailAuth foundUser = emailAuthRepository.findByEmail(email)
                .orElseThrow(()->new MemberException(MyErrorCode.INVALID_EMAIL, "이메일 인증을 요청한 적 없는 회원입니다."));
        if(!foundUser.getAuthed()) throw new MemberException(MyErrorCode.INVALID_EMAIL);
    }

}
