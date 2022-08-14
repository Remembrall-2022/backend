package com.stella.rememberall.user.kakao;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoUserService {

    private final Environment env;
    private final RestTemplate restTemplate;
    private final Gson gson;

    public KakaoProfile getKakaoProfile(String kakaoAccessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + kakaoAccessToken);

        String requestUrl = env.getProperty("social.kakao.url.profile"); // ??
        if (requestUrl == null) throw new KakaoException(KakaoErrorCode.COMMUNICATE_FAIL);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(null, headers);
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, request, String.class);
            if (response.getStatusCode() == HttpStatus.OK)
                return gson.fromJson(response.getBody(), KakaoProfile.class);
        } catch (Exception e) {
            log.error(e.toString());
            throw new KakaoException(KakaoErrorCode.COMMUNICATE_FAIL);
        }
        throw new KakaoException(KakaoErrorCode.COMMUNICATE_FAIL);
    }

    public KakaoProfile getFakeKakaoProfile(){
        String json = "{'id':1860827414,'connected_at':'2021-08-22T15:22:52Z','properties':{'nickname':'최운식'},'kakao_account':{'profile_nickname_needs_agreement':false,'profile':{'nickname':'최운식'},'has_email':true,'email_needs_agreement':false,'is_email_valid':true,'is_email_verified':true,'email':'gfdgdg@gmail.com'}}";
        try {
           return gson.fromJson(json, KakaoProfile.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}