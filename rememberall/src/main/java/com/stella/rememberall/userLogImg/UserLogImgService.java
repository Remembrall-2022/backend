package com.stella.rememberall.userLogImg;

import com.stella.rememberall.placelog.Place;
import com.stella.rememberall.placelog.PlaceLog;
import com.stella.rememberall.userLogImg.exception.EmptyFileException;
import com.stella.rememberall.userLogImg.exception.FileErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserLogImgService {
    private final S3Util s3Util;
    private final UserLogImgRepository userLogImgRepository;

    @Transactional
    public void saveUserLogImgList(PlaceLog placeLog, List<MultipartFile> multipartFileList){
        for(MultipartFile multipartFile : multipartFileList) {
            String url = s3Util.uploadFileV1("user-log-image", multipartFile);
            UserLogImg userLogImg = userLogImgRepository.save(UserLogImgSaveRequestVO.of(url, multipartFileList.indexOf(multipartFile), placeLog));
        }
    }

    @Transactional
    public void saveUserLogImgAllowsNull(PlaceLog placeLog, MultipartFile multipartFile){
        String url = null;
        if(multipartFile != null && !multipartFile.isEmpty())
            url = s3Util.uploadFileV1("user-log-image", multipartFile);
        UserLogImg userLogImg = userLogImgRepository.save(UserLogImgSaveRequestVO.of(url, 0, placeLog));
    }

    public String getImgUrl(String fileKey){
        try {
            return s3Util.getUrl(fileKey);
        } catch (Exception e) {
            throw new EmptyFileException(FileErrorCode.FILE_NOT_FOUND, "버킷에서 파일 유실이 발생했습니다.");
        }
    }

    @Transactional
    public void deleteUserLogImg(PlaceLog placeLog){
        List<UserLogImg> placeLogList = userLogImgRepository.findAllByPlaceLog(placeLog);
        for(UserLogImg userLogImg : placeLogList) {
            s3Util.deleteFile(userLogImg.getFileKey());
            userLogImgRepository.deleteById(userLogImg.getId());
        }
    }

    @Transactional
    public void updateUserLogImg(UserLogImg userLogImg, MultipartFile multipartFile){
        String updatedFileKey = s3Util.updateFile(userLogImg.getFileKey(), "user-log-image", multipartFile);
        userLogImgRepository.save(userLogImg.updateFileKey(updatedFileKey));
    }
}
