package com.coupon.couponCreate.dto;

import com.coupon.domain.CouponStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CouponCreateResponse(
        Long id,
        String couponId,
        String couponCode,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createdDate,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime expirationDate,
        BigDecimal discountAmount,
        CouponStatus status
) {}
