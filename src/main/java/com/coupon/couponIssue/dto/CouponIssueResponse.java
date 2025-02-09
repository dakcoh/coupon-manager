package com.coupon.couponIssue.dto;

import com.coupon.domain.CouponStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 쿠폰 지급 결과를 클라이언트에 전달하는 응답 DTO입니다.
 */
@Data
@Builder
public class CouponIssueResponse {
    private String couponId;                // 원본 쿠폰 ID
    private LocalDateTime issuedDate;       // 사용자에게 지급된 날짜
    private LocalDateTime expirationDate;   // 쿠폰 만료 날짜
    private double discountAmount;          // 할인 금액
    private CouponStatus status;            // 쿠폰 상태 (발급됨, 사용됨 등)
}
