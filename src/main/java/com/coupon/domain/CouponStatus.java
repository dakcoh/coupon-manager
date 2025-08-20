package com.coupon.domain;

import lombok.Getter;

@Getter
public enum CouponStatus {
    AVAILABLE("0001", "사용 가능") {
        @Override public boolean canTransitTo(CouponStatus target) {
            return target == USED || target == EXPIRED;
        }
    },
    USED("0002", "사용 완료") {
        @Override public boolean canTransitTo(CouponStatus target) {
            return false;
        }
    },
    EXPIRED("0003", "만료됨") {
        @Override public boolean canTransitTo(CouponStatus target) {
            return false;
        }
    };

    private final String code;
    private final String description;

    CouponStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static CouponStatus fromCode(String code) {
        for (CouponStatus status : CouponStatus.values()) {
            if (status.code.equals(code)) return status;
        }
        throw new IllegalArgumentException("Unknown CouponStatus code: " + code);
    }

    public abstract boolean canTransitTo(CouponStatus target);

    public void validateTransition(CouponStatus target) {
        if (!canTransitTo(target)) {
            throw new IllegalStateException("Invalid transition: " + this + " -> " + target);
        }
    }
}
