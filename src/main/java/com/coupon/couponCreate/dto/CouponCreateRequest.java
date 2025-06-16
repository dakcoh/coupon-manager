package com.coupon.couponCreate.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CouponCreateRequest {

    @NotNull
    private String couponId;

    @NotNull
    private String couponCode;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Future(message = "만료일은 현재 시각 이후여야 합니다.")
    private LocalDateTime expirationDate;

    @NotNull
    private double discountAmount;
}
