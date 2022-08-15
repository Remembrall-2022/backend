package com.stella.rememberall.user.kakao;

import com.google.gson.Gson;
import com.stella.rememberall.common.exception.internalServer.InternalServerErrorCode;
import com.stella.rememberall.common.exception.internalServer.InternalServerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoUserService {

    private final RestTemplate restTemplate;
    private final Gson gson;

    public KakaoProfile getKakaoProfile(String kakaoAccessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + kakaoAccessToken);

        String requestUrl = "https://kapi.kakao.com/v2/user/me";
        if (requestUrl == null) throw new InternalServerException(InternalServerErrorCode.COMMUNICATE_FAIL, "카카오 서버와 통신할 수 없습니다.");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(null, headers);
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, request, String.class);
            if (response.getStatusCode() == HttpStatus.OK)
                return gson.fromJson(response.getBody(), KakaoProfile.class);
        } catch (Exception e) {
            log.error(e.toString());
            throw new InternalServerException(InternalServerErrorCode.COMMUNICATE_FAIL, "카카오 서버와 통신할 수 없습니다.");
        }
        throw new InternalServerException(InternalServerErrorCode.COMMUNICATE_FAIL, "카카오 서버와 통신할 수 없습니다.");
    }

    public void kakaoUnlink(String accessToken) {
        String unlinkUrl = "https://kapi.kakao.com/v1/user/unlink";
        if (unlinkUrl == null) throw new InternalServerException(InternalServerErrorCode.COMMUNICATE_FAIL, "카카오 서버와 통신할 수 없습니다.");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(unlinkUrl, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) return;
        throw new InternalServerException(InternalServerErrorCode.COMMUNICATE_FAIL, "카카오 서버와 통신할 수 없습니다.");
    }

}