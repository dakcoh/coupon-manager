package com.coupon.couponCreate.dto;

import com.coupon.domain.CouponStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 쿠폰 생성 결과를 클라이언트에 전달하는 응답 DTO입니다.
 */
@Data
@Builder
public class CouponCreateResponse {
    private Long id;
    private String couponId;
    private String couponCode;
    private LocalDateTime createdDate;
    private LocalDateTime expirationDate;
    private double discountAmount;
    private CouponStatus status;
}
