package com.coupon.couponCreate.service;

import com.coupon.couponCreate.dto.CouponCreateRequest;
import com.coupon.couponCreate.repository.CouponCreateRepository;
import com.coupon.domain.Coupon;
import com.coupon.domain.CouponStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.util.UUID;

/**
 * 쿠폰 생성 비즈니스 로직을 담당하는 서비스입니다.
 */
@Service
@RequiredArgsConstructor
public class CouponCreateService {

    private final CouponCreateRepository couponCreateRepository;

    @Transactional
    public Coupon createCoupon(CouponCreateRequest request) {
        // UUID를 사용해 고유 식별자를 생성합니다.
        String couponId = UUID.randomUUID().toString();

        Coupon coupon = Coupon.builder()
                .couponId(couponId)
                .couponCode(request.getCouponCode())
                .expirationDate(request.getExpirationDate())
                .discountAmount(request.getDiscountAmount())
                .status(CouponStatus.fromCode("0001"))
                .build();

        try {
            return couponCreateRepository.save(coupon);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("중복된 쿠폰 코드입니다.");
        }
    }
}
