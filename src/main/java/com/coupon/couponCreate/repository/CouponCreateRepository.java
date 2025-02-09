package com.coupon.couponCreate.repository;

import com.coupon.domain.Coupon;
import com.coupon.domain.User;
import com.coupon.domain.UserCoupon;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 쿠폰 생성 시 사용하는 JPA 리포지토리입니다.
 */
@Repository
public interface CouponCreateRepository extends JpaRepository<Coupon, Long> {
    // couponId를 기준으로 조회하는 메서드 추가
    Optional<Coupon> findByCouponId(String couponId);
}
