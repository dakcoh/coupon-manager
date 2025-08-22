package com.coupon.couponIssue.repository;

import com.coupon.domain.Coupon;
import com.coupon.domain.UserCoupon;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 쿠폰 발급 관련 데이터베이스 연산을 담당하는 Repository입니다.
 */
@Repository
public interface CouponIssueRepository extends JpaRepository<UserCoupon, Long> {
    // 특정 사용자가 특정 쿠폰을 발급받았는지 여부 확인
    boolean existsByUserIdAndCouponId(String userId, String couponId);
}