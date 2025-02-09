package com.coupon.couponUse.controller;

import com.coupon.couponUse.dto.CouponUseRequest;
import com.coupon.couponUse.dto.CouponUseResponse;
import com.coupon.domain.UserCoupon;
import com.coupon.domain.mapper.CouponUseMapper;
import com.coupon.facade.CouponFacade;
import com.util.JacksonUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 쿠폰 사용 관련 API 엔드포인트를 제공하는 컨트롤러입니다.
 */
@RestController
@RequestMapping("/api/coupons/use")
@RequiredArgsConstructor
public class CouponUseController {
    private static final Logger log = LoggerFactory.getLogger(CouponUseController.class);
    private final CouponFacade couponFacade;
    private final CouponUseMapper couponUseMapper;

    /**
     * 쿠폰 사용을 처리합니다.
     */
    @PostMapping
    public ResponseEntity<CouponUseResponse> useCoupon(@RequestBody CouponUseRequest request) {
        log.info("IN : {}", JacksonUtil.toJson(request));

        UserCoupon coupon = couponFacade.useCoupon(request);
        CouponUseResponse response = couponUseMapper.toDto(coupon);

        return ResponseEntity.ok(response);
    }
}
