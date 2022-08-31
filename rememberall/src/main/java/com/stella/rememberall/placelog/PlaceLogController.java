package com.stella.rememberall.placelog;

import com.stella.rememberall.common.response.OnlyResponseString;
import com.stella.rememberall.userLogImg.UserLogImgService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class PlaceLogController {
    private final PlaceLogService placeLogService;
    private final UserLogImgService userLogImgService;

    @PostMapping("/placeLog/{placeLogId}/{imageId}")
    public void updateImg(@RequestPart(value = "file") MultipartFile multipartFile, @PathVariable Long imageId){
        userLogImgService.updateUserLogImg(imageId, multipartFile);
    }

    @PostMapping("/placeLog/new")
    public Long createPlaceLog(
            @RequestPart(value = "file") List<MultipartFile> multipartFile,
            @RequestPart @Valid PlaceLogSaveRequestDto placeLogSaveRequestDto
    ){
        return placeLogService.savePlaceLog(placeLogSaveRequestDto, multipartFile);
    }

    @GetMapping("/placeLog/{placeLogId}")
    public PlaceLogResponseDto getPlaceLog(@PathVariable Long placeLogId){
        return placeLogService.getPlaceLog(placeLogId);
    }

    @DeleteMapping("/placeLog/{placeLogId}")
    public Long deletePlaceLog(@PathVariable Long placeLogId){
        return placeLogService.deletePlaceLog(placeLogId);
    }

    @PostMapping("/placeLog/{placeLogId}/comment")
    public OnlyResponseString updatePlaceLog(
            @PathVariable Long placeLogId,
            @RequestBody Map<String, String> commentMap){
        placeLogService.updatePlaceLogComment(placeLogId, commentMap.get("comment"));
        return new OnlyResponseString("관광지별 일기의 코멘트를 수정했습니다.");
    }

    @PostMapping("/placeLog/{placeLogId}/place")
    public OnlyResponseString updatePlaceLog(
            @PathVariable Long placeLogId,
            @RequestBody @Valid PlaceSaveRequestDto requestDto){
        placeLogService.updatePlace(requestDto);
        return new OnlyResponseString("관광지별 일기의 관광지 정보를 수정했습니다.");
    }

    @PostMapping("/placeLog/{placeLogId}/userImg/{userLogImgId}")
    public OnlyResponseString updatePlaceLogUserImg(
            @PathVariable Long placeLogId, @PathVariable Long userLogImgId,
            @RequestPart(value = "file") MultipartFile multipartFile
    ){
        placeLogService.updateUserImg(placeLogId, userLogImgId, multipartFile);
        return new OnlyResponseString("관광지별 일기의 이미지를 수정했습니다.");
    }

}
