package com.coupon.domain.mapper;

import com.coupon.couponUse.dto.CouponUseResponse;
import com.coupon.domain.UserCoupon;
import org.springframework.stereotype.Component;

@Component
public class CouponUseMapper {

    public CouponUseResponse toDto(UserCoupon uc) {
        return new CouponUseResponse(
                uc.getId(),
                uc.getCouponId(),
                uc.getUserId(),
                uc.getIssuedDate(),
                uc.getExpirationDate(),
                uc.getUsedDate(),
                uc.getStatus()
        );
    }
}
