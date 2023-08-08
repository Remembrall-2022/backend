package com.stella.rememberall.userLogImg;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLogImgResponseDto {
    private Long userLogImgId;
    private String imgUrl;

    public static UserLogImgResponseDto of(Long userLogImgId, String imgUrl){
        return UserLogImgResponseDto.builder()
                .userLogImgId(userLogImgId)
                .imgUrl(imgUrl)
                .build();
    }

}
