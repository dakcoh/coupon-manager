package com.coupon.domain;

import lombok.Getter;

@Getter
public enum CouponStatus {
    AVAILABLE("0001", "사용 가능"),
    USED("0002", "사용 완료"),
    EXPIRED("0003", "만료됨");

    private final String code;
    private final String description;

    CouponStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    // 코드로 CouponStatus를 찾는 유틸리티 메서드
    public static CouponStatus fromCode(String code) {
        for (CouponStatus status : CouponStatus.values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown CouponStatus code: " + code);
    }
}
