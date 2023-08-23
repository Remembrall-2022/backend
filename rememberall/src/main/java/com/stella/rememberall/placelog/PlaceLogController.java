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

    @PostMapping("/dateLog/{dateLogId}/placeLog/new")
    public OnlyResponseString createPlaceLog(
            @PathVariable Long dateLogId,
            @RequestPart(value = "file") List<MultipartFile> multipartFile,
            @RequestPart @Valid PlaceLogSaveRequestDto placeLogSaveRequestDto ){
        Long placeLogId = placeLogService.savePlaceLog(dateLogId, placeLogSaveRequestDto, multipartFile);
        return new OnlyResponseString("장소항목을 생성했습니다. id: " + placeLogId);
    }

    @GetMapping("/placeLog/{placeLogId}")
    public PlaceLogResponseDto getPlaceLog(@PathVariable Long placeLogId){
        return placeLogService.getPlaceLog(placeLogId);
    }

    @DeleteMapping("/placeLog/{placeLogId}")
    public OnlyResponseString deletePlaceLog(@PathVariable Long placeLogId){
        placeLogService.deletePlaceLog(placeLogId);
        return new OnlyResponseString("장소 항목을 삭제했습니다.");
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
        placeLogService.updatePlace(placeLogId, requestDto);
        return new OnlyResponseString("관광지별 일기의 관광지 정보를 수정했습니다.");
    }

    @PostMapping("/userImg/{userLogImgId}")
    public OnlyResponseString updatePlaceLogUserImg(
            @PathVariable Long userLogImgId,
            @RequestPart(value = "file") MultipartFile multipartFile
    ){
        placeLogService.updateUserImg(userLogImgId, multipartFile);
        return new OnlyResponseString("관광지별 일기의 이미지를 수정했습니다.");
    }

}
