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
     * 1. 데이터베이스에서 UserCoupon 엔티티를 조회합니다.
     * 2. 쿠폰의 유효성(사용 가능 상태 및 만료 여부)을 확인합니다.
     * 3. 사용 가능한 경우, 빌더 패턴을 사용하여 새로운 객체를 생성하고 상태를 변경합니다.
     * 4. 업데이트된 UserCoupon 엔티티를 저장 후 반환합니다.
     */
    @Transactional
    public UserCoupon useCoupon(CouponUseRequest request) {
        UserCoupon userCoupon = couponIssueRepository.findByCouponId(request.getCouponId())
                .orElseThrow(() -> new IllegalArgumentException("쿠폰을 찾을 수 없습니다."));

        // 해당 사용자가 발급받은 쿠폰인지 검증
        if (!userCoupon.getUserId().equals(request.getUserId())) {
            throw new IllegalStateException("해당 쿠폰은 다른 사용자가 발급받았습니다.");
        }

        if (userCoupon.getStatus() != CouponStatus.fromCode("0001")) {
            throw new IllegalStateException("이미 사용되었거나 만료된 쿠폰입니다.");
        }

        // 빌더 패턴을 사용하여 새로운 UserCoupon 객체를 생성하면서 상태 변경
        UserCoupon updatedCoupon = UserCoupon.builder()
                .id(userCoupon.getId())
                .couponId(request.getCouponId())
                .userId(userCoupon.getUserId())
                .expirationDate(userCoupon.getExpirationDate())
                .issuedDate(userCoupon.getIssuedDate())
                .usedDate(LocalDateTime.now())
                .status(CouponStatus.fromCode("0002"))  // 사용 완료 상태 변경
                .build();

        return couponUseRepository.save(updatedCoupon);
    }
}
