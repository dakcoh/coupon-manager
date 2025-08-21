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

    public CouponIssueResponse toDto(UserCoupon uc) {
        return new CouponIssueResponse(
                uc.getCouponId(),
                uc.getIssuedDate(),
                uc.getExpirationDate(),
                uc.getStatus()
        );
    }
}
