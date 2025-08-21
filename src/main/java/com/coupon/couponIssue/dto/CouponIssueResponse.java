package com.coupon.couponIssue.dto;

import com.coupon.domain.CouponStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record CouponIssueResponse (
        String couponId,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime issuedDate,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime expirationDate,
        CouponStatus status
) {}
