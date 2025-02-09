package com.coupon.couponIssue.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 쿠폰 발급 요청을 처리하는 DTO입니다.
 */
@Getter
@Setter
@AllArgsConstructor
public class CouponIssueRequest {
    private String couponId; // 발급할 쿠폰 ID
    private String userId;   // 발급받을 사용자 ID
}