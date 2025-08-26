package com.coupon.couponCreate.repository;

import com.coupon.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponCreateRepository extends JpaRepository<Coupon, Long> {
}
