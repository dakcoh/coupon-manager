package com.coupon.couponUse.service;

import com.coupon.couponUse.repository.CouponUseRepository;
import com.coupon.domain.UserCoupon;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponUseService {

    private final CouponUseRepository repository;

    @Transactional
    public UserCoupon useCoupon(Long userCouponId, String userId) {
        UserCoupon uc = repository.findByIdForUpdate(userCouponId)
                .orElseThrow(() -> new IllegalArgumentException("발급 쿠폰을 찾을 수 없습니다."));

        if (!uc.getUserId().equals(userId)) {
            throw new IllegalStateException("해당 사용자의 쿠폰이 아닙니다.");
        }
        uc.expire();
        uc.use();

        return uc;
    }
}
