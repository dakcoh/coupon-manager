package com.coupon.facade;

import com.coupon.couponIssue.dto.CouponIssueRequest;
import com.coupon.couponUse.dto.CouponUseRequest;
import com.coupon.domain.Coupon;
import com.coupon.couponCreate.dto.CouponCreateRequest;
import com.coupon.couponCreate.service.CouponCreateService;
import com.coupon.couponIssue.service.CouponIssueService;
import com.coupon.couponUse.service.CouponUseService;
import com.coupon.domain.UserCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponFacade {

    private final CouponCreateService couponCreateService;
    private final CouponIssueService couponIssueService;
    private final CouponUseService couponUseService;

    /**
     * 쿠폰 생성 요청을 처리합니다.
     */
    public Coupon createCoupon(CouponCreateRequest request) {
        return couponCreateService.createCoupon(request);
    }

    /**
     * 특정 쿠폰을 사용자에게 지급합니다.
     */
    public UserCoupon issueCoupon(String couponId, String userId) {
        return couponIssueService.issueCoupon(couponId, userId);
    }

    /**
     * 사용자가 쿠폰을 사용하도록 처리합니다.
     */
    public UserCoupon useCoupon(CouponUseRequest request) {
        return couponUseService.useCoupon(request);
    }
}
