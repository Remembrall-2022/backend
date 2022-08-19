package com.stella.rememberall.userLogImg;

import com.stella.rememberall.common.response.OnlyResponseString;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class UserLogController {
    private final UserLogService userLogService;

    @PostMapping("/placelog/img")
    public List<String> uploadFile(
            @RequestPart(value = "file") List<MultipartFile> multipartFile) {
        return userLogService.saveUserLogImg(multipartFile);
    }

    @DeleteMapping("/placelog/img")
    public OnlyResponseString deleteFile(){
        userLogService.deleteUserLogImg();
        return new OnlyResponseString("이미지를 버킷에서 삭제했습니다.");
    }


}
