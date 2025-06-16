package com.coupon.couponCreate.controller;

import com.coupon.couponCreate.dto.CouponCreateRequest;
import com.coupon.couponCreate.dto.CouponCreateResponse;
import com.coupon.domain.Coupon;
import com.coupon.domain.mapper.CouponCreateMapper;
import com.coupon.facade.CouponFacade;
import com.util.JacksonUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;

/**
 * 쿠폰 생성 관련 API 엔드포인트를 제공하는 컨트롤러입니다.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CouponCreateController {
    private static final Logger log = LoggerFactory.getLogger(CouponCreateController.class);
    private final CouponCreateMapper couponCreateMapper;
    private final CouponFacade couponFacade;

    /**
     * 쿠폰을 생성합니다.
     */
    @RequestMapping(value = "/coupons/create", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<CouponCreateResponse> createCoupon(@RequestBody CouponCreateRequest request) {
        log.info("IN : {}", JacksonUtil.toJson(request));

        Coupon coupon = couponFacade.createCoupon(request);
        CouponCreateResponse response = couponCreateMapper.toDto(coupon);

        log.info("OUT : {}", JacksonUtil.toJson(response));

        return ResponseEntity.ok(response);
        }
}
