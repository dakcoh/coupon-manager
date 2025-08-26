package com.coupon.couponUse.dto;

import jakarta.validation.constraints.NotBlank;

public record CouponUseRequest (
    @NotBlank String userId
) {}