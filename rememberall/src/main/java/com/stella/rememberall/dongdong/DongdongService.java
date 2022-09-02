package com.stella.rememberall.dongdong;

import com.stella.rememberall.user.UserService;
import com.stella.rememberall.user.domain.User;
import com.stella.rememberall.user.exception.MemberException;
import com.stella.rememberall.user.exception.MyErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.stella.rememberall.dongdong.DongdongImg.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class DongdongService {

    private final DongdongRepository dongdongRepository;
    private final UserService userService;

    @Transactional
    public DongdongResponseDto readDongdong() {
        Dongdong dongdongEntity = userService.getLoginedUser().getDongdong();
        DongdongLevelRule rule = createLevelRule(dongdongEntity.getExp());

        return DongdongResponseDto.builder()
                .userId(dongdongEntity.getId())
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
    public DongdongResponseDto reward(DongdongReward dongdongReward) {
        User loginedUser = userService.getLoginedUser();
        Dongdong dongdong = dongdongRepository.findById(loginedUser.getId())
                .orElseThrow(() -> new MemberException(MyErrorCode.USER_NOT_FOUND));

        Long updatedExp = dongdong.getExp() + dongdongReward.getExp();
        Long updatedPoint = dongdong.getPoint() + dongdongReward.getPoint();

        dongdong.setExp(updatedExp);
        dongdong.setPoint(updatedPoint);

        DongdongLevelRule levelRule = createLevelRule(updatedExp);

        return DongdongResponseDto.builder()
                .userId(loginedUser.getId())
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

    /**둥둥이 조회시 호출, 서비스 내에서만 사용*/
    DongdongLevelRule createLevelRule(Long exp) {
        DongdongLevelRule rule;

        if (exp >= 2800)
            rule = new DongdongLevelRule(10, 2800L, STEP3.getImgUrl());
        else if (exp >= 2400)
            rule = new DongdongLevelRule(9, 2400L, STEP2.getImgUrl());
        else if (exp >= 2200)
            rule = new DongdongLevelRule(8, 2200L, STEP2.getImgUrl());
        else if (exp >= 2000)
            rule = new DongdongLevelRule(7, 2000L, STEP2.getImgUrl());
        else if (exp >= 1800)
            rule = new DongdongLevelRule(6, 1800L, STEP2.getImgUrl());
        else if (exp >= 1600)
            rule = new DongdongLevelRule(5, 1600L, STEP2.getImgUrl());
        else if (exp >= 1400)
            rule = new DongdongLevelRule(4, 1400L, STEP1.getImgUrl());
        else if (exp >= 1200)
            rule = new DongdongLevelRule(3, 1200L, STEP1.getImgUrl());
        else if (exp >= 600)
            rule = new DongdongLevelRule(2, 600L, STEP1.getImgUrl());
        else if (exp >= 300)
            rule = new DongdongLevelRule(1, 300L, STEP1.getImgUrl());
        else
            rule = new DongdongLevelRule(0, 0L, STEP0.getImgUrl());

        return rule;
    }
}
