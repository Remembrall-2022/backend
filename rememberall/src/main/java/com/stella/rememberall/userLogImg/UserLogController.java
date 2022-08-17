package com.stella.rememberall.userLogImg;

import com.stella.rememberall.common.response.OnlyResponseString;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class UserLogController {
    private final UserLogService userLogService;

    @PostMapping("/placelog/img")
    public OnlyResponseString uploadFile(
            @RequestPart(value = "file") MultipartFile multipartFile) {
        return new OnlyResponseString(userLogService.saveUserLogImg(multipartFile));
    }
}
