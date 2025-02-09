package com.coupon.couponUse.repository;

import com.coupon.domain.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Coupon 도메인 객체의 CRUD 작업을 담당하는 JPA Repository입니다.
 */
@Repository
public interface CouponUseRepository extends JpaRepository<UserCoupon, Long> {
}
