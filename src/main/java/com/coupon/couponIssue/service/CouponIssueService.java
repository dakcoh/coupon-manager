package com.coupon.couponIssue.service;

import com.coupon.couponIssue.repository.CouponIssueRepository;
import com.coupon.domain.Coupon;
import com.coupon.domain.UserCoupon;
import com.coupon.domain.repository.CouponRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.coupon.config.RedisLockManager;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class CouponIssueService {
    private static final String LOCK_PREFIX = "lock:coupon:issue:";
    private static final Duration LOCK_TTL = Duration.ofSeconds(10);

    private final CouponIssueRepository couponIssueRepository;
    private final CouponRepository couponRepository;
    private final RedisLockManager lockManager;

    @Transactional
    public UserCoupon issueCoupon(String couponId, String userId) {
        String lockKey = LOCK_PREFIX + couponId;

        // 락 획득
        String token = lockManager.acquire(lockKey, LOCK_TTL);
        if (token == null) {
            throw new IllegalStateException("다른 요청이 진행 중입니다. 잠시 후 다시 시도해 주세요.");
        }

        try {
            // 원본 쿠폰 조회
            Coupon coupon = couponRepository.findByCouponId(couponId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 쿠폰을 찾을 수 없습니다."));

            // 도메인 규칙 위임
            coupon.ensureIssuable();

            // 중복 발급 방지
            if (couponIssueRepository.existsByUserIdAndCouponId(userId, couponId)) {
                throw new IllegalStateException("이미 발급받은 쿠폰입니다.");
            }

            UserCoupon userCoupon = UserCoupon.issueFrom(coupon, userId);

            return couponIssueRepository.save(userCoupon);
        } finally {
            // 소유자 토큰 일치 시에만 안전하게 해제
            lockManager.release(lockKey, token);
        }
    }
}
