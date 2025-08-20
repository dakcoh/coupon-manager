package com.coupon.couponCreate.service;

import com.coupon.couponCreate.dto.CouponCreateRequest;
import com.coupon.couponCreate.repository.CouponCreateRepository;
import com.coupon.domain.Coupon;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

/**
 * 쿠폰 생성 비즈니스 로직을 담당하는 서비스입니다.
 */
@Service
@RequiredArgsConstructor
public class CouponCreateService {

    private final CouponCreateRepository couponCreateRepository;

    @Transactional
    public Coupon createCoupon(CouponCreateRequest request) {
        Coupon coupon = Coupon.create(request.couponCode(), request.expirationDate(), request.discountAmount());

        try {
            return couponCreateRepository.save(coupon);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("중복된 쿠폰 코드입니다." + request.couponCode(), e);
        }
    }
}
