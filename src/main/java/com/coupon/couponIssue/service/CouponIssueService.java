package com.coupon.couponIssue.service;

import com.coupon.couponCreate.repository.CouponCreateRepository;
import com.coupon.couponIssue.dto.CouponIssueRequest;
import com.coupon.couponIssue.repository.CouponIssueRepository;
import com.coupon.domain.Coupon;
import com.coupon.domain.CouponStatus;
import com.coupon.domain.UserCoupon;
import com.coupon.domain.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * 쿠폰 지급 로직을 처리하는 서비스입니다.
 * Redis를 이용하여 분산 락을 적용함으로써 동시 요청 시 데이터 정합성을 보장합니다.
 */
@Service
@RequiredArgsConstructor
public class CouponIssueService {

    private final CouponIssueRepository couponIssueRepository;
    private final CouponCreateRepository couponCreateRepository;
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * ✅ [1] **분산 락을 사용한 쿠폰 발급 (Redis 활용)**
     * 1. Redis를 통해 분산 락(lock:coupon:issue:{couponId})을 획득합니다.
     * 2. 데이터베이스에서 원본 쿠폰을 조회하고, 발급 가능한 상태인지 확인합니다.
     * 3. 지급 처리 후 새로운 UserCoupon을 생성하여 저장합니다.
     * 4. 최종적으로 락을 해제합니다.
     */
    @Transactional
    public UserCoupon issueCoupon(CouponIssueRequest request) {
        String lockKey = "lock:coupon:issue:" + request.getCouponId();

        // Redis의 setIfAbsent를 통해 분산 락을 획득 (10초의 만료시간 적용)
        Boolean lockAcquired = redisTemplate.opsForValue().setIfAbsent(lockKey, request.getUserId(), Duration.ofSeconds(10));
        if (lockAcquired == null || !lockAcquired) {
            throw new IllegalStateException("다른 요청이 진행 중입니다. 잠시 후 다시 시도해 주세요.");
        }

        try {
            // 원본 쿠폰 조회
            Coupon coupon = couponCreateRepository.findByCouponId(request.getCouponId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 쿠폰을 찾을 수 없습니다."));

            // 쿠폰이 만료되지 않았는지 확인
            if (LocalDateTime.now().isAfter(coupon.getExpirationDate())) {
                throw new IllegalStateException("해당 쿠폰은 이미 만료되었습니다.");
            }

            // 동일한 사용자가 이미 해당 쿠폰을 발급받았는지 확인
            if (couponIssueRepository.existsByUserIdAndCouponId(request.getUserId(), request.getCouponId())) {
                throw new IllegalStateException("이미 발급받은 쿠폰입니다.");
            }

            UserCoupon userCoupon = UserCoupon.builder()
                    .couponId(request.getCouponId())
                    .userId(request.getUserId())
                    .issuedDate(LocalDateTime.now())
                    .expirationDate(coupon.getExpirationDate())
                    .status(CouponStatus.fromCode("0001")) // 발급된 상태 (AVAILABLE)
                    .build();

            // UserCoupon 저장
            return couponIssueRepository.save(userCoupon);

        } finally {
            // 분산 락 해제
            redisTemplate.delete(lockKey);
        }
    }
}
