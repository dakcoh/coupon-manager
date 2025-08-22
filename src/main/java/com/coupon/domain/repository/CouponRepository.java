package com.coupon.domain.repository;

import com.coupon.domain.Coupon;
import com.coupon.domain.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Optional<Coupon> findByCouponId(String couponId);
}
