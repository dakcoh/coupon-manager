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

    public CouponCreateResponse toDto(Coupon coupon) {
        return CouponCreateResponse.builder()
                .id(coupon.getId())
                .couponId(coupon.getCouponId())
                .couponCode(coupon.getCouponCode())
                .createdDate(coupon.getCreatedDate())
                .expirationDate(coupon.getExpirationDate())
                .discountAmount(coupon.getDiscountAmount())
                .status(coupon.getStatus())
                .build();
    }
}
