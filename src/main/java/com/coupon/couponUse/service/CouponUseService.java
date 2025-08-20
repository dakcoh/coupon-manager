package com.coupon.couponUse.service;

import com.coupon.couponCreate.repository.CouponCreateRepository;
import com.coupon.couponIssue.repository.CouponIssueRepository;
import com.coupon.couponUse.dto.CouponUseRequest;
import com.coupon.couponUse.repository.CouponUseRepository;
import com.coupon.domain.UserCoupon;
import com.coupon.domain.CouponStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CouponUseService {

    private final CouponUseRepository couponUseRepository;
    private final CouponIssueRepository couponIssueRepository;

    /**
     * 주어진 couponId에 대해 쿠폰 사용을 처리합니다.
     * 1. 쿠폰 조회
     * 2. 쿠폰 유효성 검사
     * 3. 상태 변경
     * 4. JPA dirty checking
     */
    @Transactional
    public UserCoupon useCoupon(CouponUseRequest request) {
        UserCoupon userCoupon = couponIssueRepository.findByCouponId(request.getCouponId())
                .orElseThrow(() -> new IllegalArgumentException("쿠폰을 찾을 수 없습니다."));

        if (!userCoupon.getUserId().equals(request.getUserId())) {
            throw new IllegalStateException("해당 쿠폰은 다른 사용자가 발급받았습니다.");
        }
        if (userCoupon.isExpired()) {
            throw new IllegalStateException("쿠폰이 만료되었습니다.");
        }

        userCoupon.transit(CouponStatus.USED);

        return userCoupon;
    }
}
