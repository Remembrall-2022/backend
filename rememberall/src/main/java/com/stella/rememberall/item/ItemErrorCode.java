package com.stella.rememberall.item;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ItemErrorCode {

    ITEM_NOT_FOUND("아이템을 찾을 수 없습니다.");

    private String defaultMessage;
}
