package com.coupon.domain.mapper;

import com.coupon.domain.Coupon;
import com.coupon.couponCreate.dto.CouponCreateResponse;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Builder
@Component
public class CouponCreateMapper {
    public CouponCreateResponse toDto(Coupon c) {
        return new CouponCreateResponse(
                c.getId(),
                c.getCouponId(),
                c.getCouponCode(),
                c.getCreatedDate(),
                c.getExpirationDate(),
                c.getDiscountAmount(),
                c.getStatus()
        );
    }
}