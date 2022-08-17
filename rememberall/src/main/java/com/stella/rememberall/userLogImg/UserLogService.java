package com.stella.rememberall.userLogImg;

import com.stella.rememberall.common.response.OnlyResponseString;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserLogService {
    private final S3Util s3Util;

    @Transactional
    public String saveUserLogImg(MultipartFile multipartFile){
        // TODO : url 받아서 UserLogImg Entity 생성하여 save
        return s3Util.uploadFileV1("user-log-image", multipartFile);
    }
}
