package com.stella.rememberall.userLogImg;

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

    @Transactional
    public void deleteUserLogImg(){ // 저장한 UserLogImg의 Id가 들어온다고 가정
        // TODO : 저장한 UserLogImg Entity를 id로 조회해서 fileKey 가져옴
        String fileKey = "user-log-image/2d675af6-544f-4624-bad8-c417a23c98aa-download (2).jpeg";
        s3Util.deleteFile(fileKey);
    }
}
