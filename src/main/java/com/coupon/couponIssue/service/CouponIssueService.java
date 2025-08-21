package com.coupon.couponIssue.service;

import com.coupon.couponIssue.repository.CouponIssueRepository;
import com.coupon.domain.Coupon;
import com.coupon.domain.UserCoupon;
import com.coupon.domain.repository.CouponRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.UUID;

/**
 * 쿠폰 지급 로직을 처리하는 서비스입니다.
 * Redis를 이용하여 분산 락을 적용함으로써 동시 요청 시 데이터 정합성을 보장합니다.
 */
@Service
@RequiredArgsConstructor
public class CouponIssueService {
    private static final String LOCK_PREFIX = "lock:coupon:issue:";
    private static final Duration LOCK_TTL = Duration.ofSeconds(10);

    private final CouponIssueRepository couponIssueRepository;
    private final CouponRepository couponRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public UserCoupon issueCoupon(String couponId, String userId) {
        String lockKey = LOCK_PREFIX + couponId;
        String token = UUID.randomUUID().toString();

        Boolean lockAcquired = redisTemplate.opsForValue().setIfAbsent(lockKey, token, LOCK_TTL);

        if (!Boolean.TRUE.equals(lockAcquired)) throw new IllegalStateException("다른 요청이 진행 중입니다. 잠시 후 다시 시도해 주세요.");

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
            String owner = redisTemplate.opsForValue().get(lockKey);
            if (token.equals(owner)) {
                redisTemplate.delete(lockKey);
            }
        }
    }
}
