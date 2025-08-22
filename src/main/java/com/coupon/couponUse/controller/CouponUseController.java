package com.coupon.couponUse.controller;

import com.coupon.couponUse.dto.CouponUseRequest;
import com.coupon.couponUse.dto.CouponUseResponse;
import com.coupon.domain.UserCoupon;
import com.coupon.domain.mapper.CouponUseMapper;
import com.coupon.facade.CouponFacade;
import com.util.JacksonUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * 쿠폰 사용 관련 API 엔드포인트를 제공하는 컨트롤러입니다.
 */
@RestController
@RequestMapping("/api/user-coupons")
@RequiredArgsConstructor
public class CouponUseController {
    private static final Logger log = LoggerFactory.getLogger(CouponUseController.class);
    private final CouponFacade facade;
    private final CouponUseMapper mapper;

    @PostMapping(value = "/{userCouponId}/use", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<CouponUseResponse> useCoupon(@PathVariable Long userCouponId, @Valid @RequestBody CouponUseRequest request) {
        log.info("{} IN : {}", userCouponId, JacksonUtil.toJson(request));

        UserCoupon saved = facade.useCoupon(userCouponId, request.userId());
        CouponUseResponse response = mapper.toDto(saved);

        log.info("{} OUT : {}", userCouponId, JacksonUtil.toJson(response));

        return ResponseEntity.ok(response);
    }
}