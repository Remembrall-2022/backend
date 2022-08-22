package com.stella.rememberall.placelog;

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
    public Long createTripLog(
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
    public Long updatePlaceLog(
            @PathVariable Long placeLogId,
            @RequestBody Map<String, String> commentMap){
        return placeLogService.updatePlaceLogComment(placeLogId, commentMap.get("comment"));
    }

    @PostMapping("/placeLog/{placeLogId}/place")
    public Long updatePlaceLog(
            @RequestBody PlaceSaveRequestDto requestDto){
        return placeLogService.updatePlace(requestDto);
    }

    @PostMapping("/placeLog/{placeLogId}")
    public Long updatePlaceLog(
            @PathVariable Long placeLogId,
            @RequestBody PlaceLogSaveRequestDto requestDto){
        return placeLogService.updatePlaceLog(placeLogId, requestDto);
    }

}
