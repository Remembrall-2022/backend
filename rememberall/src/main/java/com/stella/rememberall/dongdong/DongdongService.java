package com.stella.rememberall.dongdong;

import com.stella.rememberall.user.domain.User;
import com.stella.rememberall.user.exception.MemberException;
import com.stella.rememberall.user.exception.MyErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.stella.rememberall.dongdong.DongdongExCode.DONGDONG_IMG_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class DongdongService {

    private final DongdongRepository dongdongRepository;
    private final DongdongImgRepository dongdongImgRepository;

    DongdongResponseDto readDongdong(Long userId) {
        Dongdong dongdongEntity = dongdongRepository.findById(userId)
                .orElseThrow(() -> new MemberException(MyErrorCode.USER_NOT_FOUND));
        return DongdongResponseDto.builder()
                .user(dongdongEntity.getUser())
                .exp(dongdongEntity.getExp())
                .point(dongdongEntity.getPoint())
                .dongdongImg(dongdongEntity.getDongdongImg())
                .level(calLv(dongdongEntity.getExp()))
                .build();
    }

    /**
     * 서버에서 내부적으로 호출할 둥둥이 로직들, 컨트롤러에서 호출금지
     * 사실 분리하는게 더 좋을 것 같음
     * */

    /**유저 생성시 호출*/
    Long createDongdong(User user) {
        Dongdong dongdong = new Dongdong(user);
        //둥둥이이미지 아이디 1번 -> 둥둥이 초기 이미지 lv0
        dongdong.setDongdongImg(dongdongImgRepository.findById(1L).orElseThrow(() -> new DongdongException(DONGDONG_IMG_NOT_FOUND)));
        dongdongRepository.save(dongdong);
        return user.getId();
    }

    /**일기 생성시 호출*/
    Long addExp(User user, Long exp) {
        Dongdong dongdong = dongdongRepository.findById(user.getId())
                .orElseThrow(() -> new MemberException(MyErrorCode.USER_NOT_FOUND));
        Long updateExp =  dongdong.getExp() + exp;
        dongdong.setExp(updateExp);
        return updateExp;
    }

    /**둥둥이 조회시 호출*/
    //가독성 난리남 방법 없을까
    Integer calLv(Long exp) {
        Integer level = -1;

        if (exp < 300) level = 0;
        else if (exp < 600) level = 1;
        else if (exp < 1200) level = 2;
        else if (exp < 1400) level = 3;
        else if (exp < 1600) level = 4;
        else if (exp < 1800) level = 5;
        else if (exp < 2000) level = 6;
        else if (exp < 2200) level = 7;
        else if (exp < 2400) level = 8;
        else if (exp < 2800) level = 9;
        else level = 10;

        return level;
    }
}
