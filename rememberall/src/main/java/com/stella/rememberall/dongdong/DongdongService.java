package com.stella.rememberall.dongdong;

import com.stella.rememberall.user.domain.User;
import com.stella.rememberall.user.exception.MemberException;
import com.stella.rememberall.user.exception.MyErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DongdongService {

    private final DongdongRepository dongdongRepository;
    private final DongdongImgRepository dongdongImgRepository;

    public DongdongResponseDto readDongdong(Long userId) {
        Dongdong dongdongEntity = dongdongRepository.findById(userId)
                .orElseThrow(() -> new MemberException(MyErrorCode.USER_NOT_FOUND));
        DongdongLevelRule rule = createLevelRule(dongdongEntity.getExp());

        return DongdongResponseDto.builder()
                .user(dongdongEntity.getUser())
                .exp(dongdongEntity.getExp())
                .point(dongdongEntity.getPoint())
                .level(rule.getLevel())
                .dongdongImg(rule.getDongdongImg())
                .build();
    }

    /**
     * 서버에서 내부적으로 호출할 둥둥이 로직들, 컨트롤러에서 호출금지
     * 사실 분리하는게 더 좋을 것 같음
     * */

    /**유저 생성시 호출*/
    public Long createDongdong(User user) {
        Dongdong dongdong = new Dongdong(user);
        dongdongRepository.save(dongdong);
        return user.getId();
    }

    /**경험치 더하기*/
    public Dongdong addExp(User user, Long exp) {
        Dongdong dongdong = dongdongRepository.findById(user.getId())
                .orElseThrow(() -> new MemberException(MyErrorCode.USER_NOT_FOUND));
        Long updateExp =  dongdong.getExp() + exp;
        dongdong.setExp(updateExp);
        return dongdong;
    }

    /**포인트 더하기*/
    public Dongdong addPoint(User user, Long point) {
        Dongdong dongdong = dongdongRepository.findById(user.getId())
                .orElseThrow(() -> new MemberException(MyErrorCode.USER_NOT_FOUND));
        Long updatePoint = dongdong.getPoint() + point;
        dongdong.setPoint(updatePoint);
        return dongdong;
    }

    /**포인트 지불*/
    public Dongdong payPoint(User user, Long point) {
        Dongdong dongdong = dongdongRepository.findById(user.getId())
                .orElseThrow(() -> new MemberException(MyErrorCode.USER_NOT_FOUND));
        if (dongdong.getPoint() >= point)
            dongdong.setPoint(dongdong.getPoint() - point);
        else
            throw new DongdongException(DongdongExCode.DONGDONG_LACK_OF_POINT);
        return dongdong;
    }

    /**둥둥이 조회시 호출, 서비스 내에서만 사용
     * TODO: url 어떻게 처리할지 다시 고민
     * */
    DongdongLevelRule createLevelRule(Long exp) {
        DongdongLevelRule rule;

        if (exp >= 2800) rule = new DongdongLevelRule(10, 2800L, dongdongImgRepository.findById(1L).get());
        else if (exp >= 2400) rule = new DongdongLevelRule(9, 2400L, dongdongImgRepository.findById(1L).get());
        else if (exp >= 2200) rule = new DongdongLevelRule(8, 2200L, dongdongImgRepository.findById(1L).get());
        else if (exp >= 2000) rule = new DongdongLevelRule(7, 2000L, dongdongImgRepository.findById(1L).get());
        else if (exp >= 1800) rule = new DongdongLevelRule(6, 1800L, dongdongImgRepository.findById(1L).get());
        else if (exp >= 1600) rule = new DongdongLevelRule(5, 1600L, dongdongImgRepository.findById(1L).get());
        else if (exp >= 1400) rule = new DongdongLevelRule(4, 1400L, dongdongImgRepository.findById(1L).get());
        else if (exp >= 1200) rule = new DongdongLevelRule(3, 1200L, dongdongImgRepository.findById(1L).get());
        else if (exp >= 600) rule = new DongdongLevelRule(2, 600L, dongdongImgRepository.findById(1L).get());
        else if (exp >= 300) rule = new DongdongLevelRule(1, 300L, dongdongImgRepository.findById(1L).get());
        else rule = new DongdongLevelRule(0, 0L, dongdongImgRepository.findById(1L).get());

        return rule;
    }
}
