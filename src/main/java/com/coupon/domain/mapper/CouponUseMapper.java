package com.coupon.domain.mapper;

import com.coupon.couponUse.dto.CouponUseResponse;
import com.coupon.domain.UserCoupon;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Builder
@Component
public class CouponUseMapper {

    public CouponUseResponse toDto(UserCoupon userCoupon) {
        return CouponUseResponse.builder()
                .couponId(userCoupon.getCouponId())
                .usedDate(userCoupon.getIssuedDate())
                .status(userCoupon.getStatus())
                .build();
    }
}
