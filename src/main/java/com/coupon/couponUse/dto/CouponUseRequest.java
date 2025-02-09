package com.coupon.couponUse.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 쿠폰 사용 요청 시 클라이언트로부터 전달받는 데이터를 담는 DTO입니다.
 */
@Data
@Builder
public class CouponUseRequest {
    private String couponId;
    private String userId;
}