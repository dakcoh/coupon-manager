package com.coupon.couponUse.dto;

import com.coupon.domain.CouponStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record CouponUseResponse (
    Long userCouponId,
    String couponId,
    String userId,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime issuedDate,
    LocalDateTime expirationDate,
    LocalDateTime usedDate,
    CouponStatus status
) {}
