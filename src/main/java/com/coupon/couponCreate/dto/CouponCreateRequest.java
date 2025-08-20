package com.coupon.couponCreate.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CouponCreateRequest(
        @NotBlank String couponCode,
        @NotNull @Future
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime expirationDate,
        @PositiveOrZero BigDecimal discountAmount
) {}