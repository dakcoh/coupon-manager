package com.coupon.domain.mapper;

import com.coupon.couponIssue.dto.CouponIssueResponse;
import com.coupon.domain.UserCoupon;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Builder
@Component
public class CouponIssueMapper {

    public CouponIssueResponse toDto(UserCoupon userCoupon) {
        return CouponIssueResponse.builder()
                .couponId(userCoupon.getCouponId())
                .issuedDate(userCoupon.getIssuedDate())
                .expirationDate(userCoupon.getExpirationDate())
                .status(userCoupon.getStatus())
                .build();
    }
}
