package com.stella.rememberall.dongdong;

import com.stella.rememberall.user.domain.User;
import com.stella.rememberall.user.exception.MemberException;
import com.stella.rememberall.user.exception.MyErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DongdongService {

    private final DongdongRepository dongdongRepository;

    @Transactional
    public DongdongResponseDto readDongdong(Long userId) {
        Dongdong dongdongEntity = dongdongRepository.findById(userId)
                .orElseThrow(() -> new MemberException(MyErrorCode.USER_NOT_FOUND));
        DongdongLevelRule rule = createLevelRule(dongdongEntity.getExp());

        return DongdongResponseDto.builder()
                .userId(userId)
                .exp(dongdongEntity.getExp())
                .point(dongdongEntity.getPoint())
                .level(rule.getLevel())
                .dongdongImgUrl(rule.getDongdongImgUrl())
                .level(rule.getLevel())
                .build();
    }

    /**
     * 서버에서 내부적으로 호출할 둥둥이 로직들, 컨트롤러에서 호출금지
     * 사실 분리하는게 더 좋을 것 같음
     * */

    /**유저 생성시 호출*/
    @Transactional
    public Long createDongdong(User user) {
        Dongdong dongdong = new Dongdong(user);
        dongdongRepository.save(dongdong);
        return user.getId();
    }

    /**리워드 지급*/
    @Transactional
    public DongdongResponseDto reward(Long userId, DongdongReward dongdongReward) {
        Dongdong dongdong = dongdongRepository.findById(userId)
                .orElseThrow(() -> new MemberException(MyErrorCode.USER_NOT_FOUND));

        Long updatedExp = dongdong.getExp() + dongdongReward.getExp();
        Long updatedPoint = dongdong.getPoint() + dongdongReward.getPoint();

        dongdong.setExp(updatedExp);
        dongdong.setPoint(updatedPoint);

        DongdongLevelRule levelRule = createLevelRule(updatedExp);

        return DongdongResponseDto.builder()
                .userId(userId)
                .exp(updatedExp)
                .point(updatedPoint)
                .dongdongImgUrl(levelRule.getDongdongImgUrl())
                .level(levelRule.getLevel())
                .build();
    }

    /**포인트 지불*/
    @Transactional
    public DongdongResponseDto payPoint(User user, Long point) {
        Dongdong dongdong = dongdongRepository.findById(user.getId())
                .orElseThrow(() -> new MemberException(MyErrorCode.USER_NOT_FOUND));
        Long updatedPoint = dongdong.getPoint();
        if (updatedPoint >= point) {
            updatedPoint = dongdong.getPoint() - point;
            dongdong.setPoint(updatedPoint);
        }
        else
            throw new DongdongException(DongdongExCode.DONGDONG_LACK_OF_POINT);
        return DongdongResponseDto.builder()
                .userId(user.getId())
                .exp(dongdong.getExp())
                .point(updatedPoint)
                .build();
    }

    /**둥둥이 조회시 호출, 서비스 내에서만 사용
     * TODO: url 어떻게 처리할지 다시 고민 -> DongdongImgRepository랑 엔티티 아예 없애고 url만 enum type으로 넣으면?
     * */
    DongdongLevelRule createLevelRule(Long exp) {
        DongdongLevelRule rule;

        if (exp >= 2800)
            rule = new DongdongLevelRule(10, 2800L, "4.png");
        else if (exp >= 2400)
            rule = new DongdongLevelRule(9, 2400L, "3.png");
        else if (exp >= 2200)
            rule = new DongdongLevelRule(8, 2200L, "3.png");
        else if (exp >= 2000)
            rule = new DongdongLevelRule(7, 2000L, "3.png");
        else if (exp >= 1800)
            rule = new DongdongLevelRule(6, 1800L, "3.png");
        else if (exp >= 1600)
            rule = new DongdongLevelRule(5, 1600L, "3.png");
        else if (exp >= 1400)
            rule = new DongdongLevelRule(4, 1400L, "2.png");
        else if (exp >= 1200)
            rule = new DongdongLevelRule(3, 1200L, "2.png");
        else if (exp >= 600)
            rule = new DongdongLevelRule(2, 600L, "2.png");
        else if (exp >= 300)
            rule = new DongdongLevelRule(1, 300L, "2.png");
        else
            rule = new DongdongLevelRule(0, 0L, "1.png");

        return rule;
    }
}
