package com.stella.rememberall.placelog;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class PlaceLogController {
    private final PlaceLogService placeLogService;

    @PostMapping("/placeLog/new")
    public Long createTripLog(@RequestBody @Valid PlaceLogSaveRequestDto dto){
        return placeLogService.savePlaceLog(dto);
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
