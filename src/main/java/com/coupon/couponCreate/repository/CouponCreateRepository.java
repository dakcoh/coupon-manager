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

@Repository
public interface CouponCreateRepository extends JpaRepository<Coupon, Long> {
}
