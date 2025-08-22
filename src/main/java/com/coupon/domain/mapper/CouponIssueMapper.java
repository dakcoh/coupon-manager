package com.coupon.domain.mapper;

import com.coupon.couponIssue.dto.CouponIssueResponse;
import com.coupon.domain.UserCoupon;
import org.springframework.stereotype.Component;

@Component
public class CouponIssueMapper {

    public CouponIssueResponse toDto(UserCoupon uc) {
        return new CouponIssueResponse(
                uc.getCouponId(),
                uc.getIssuedDate(),
                uc.getExpirationDate(),
                uc.getStatus()
        );
    }
}
