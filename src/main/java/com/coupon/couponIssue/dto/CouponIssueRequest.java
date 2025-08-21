package com.coupon.couponIssue.dto;

import jakarta.validation.constraints.NotBlank;

public record CouponIssueRequest(
    @NotBlank String couponId,
    @NotBlank String userId
) {}