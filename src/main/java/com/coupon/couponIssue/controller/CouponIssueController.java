package com.coupon.couponIssue.controller;

import com.coupon.couponIssue.dto.CouponIssueRequest;
import com.coupon.couponIssue.dto.CouponIssueResponse;
import com.coupon.domain.UserCoupon;
import com.coupon.domain.mapper.CouponIssueMapper;
import com.coupon.facade.CouponFacade;
import com.util.JacksonUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

/**
 * 쿠폰 지급 관련 API 엔드포인트를 제공하는 컨트롤러입니다.
 */
@RestController
@RequestMapping("/api/coupons/{couponId}")
@RequiredArgsConstructor
public class CouponIssueController {
    private static final Logger log = LoggerFactory.getLogger(CouponIssueController.class);

    private final CouponFacade facade;
    private final CouponIssueMapper mapper;

    /**
     * 쿠폰 지급 요청을 처리합니다.
     */
    @PostMapping("/coupons/issue")
    public ResponseEntity<CouponIssueResponse> issueCoupon(@Valid @RequestBody CouponIssueRequest request) {
        log.info("{} IN : {}", request.couponId(), JacksonUtil.toJson(request));

        UserCoupon saved = facade.issueCoupon(request.couponId(), request.userId());
        CouponIssueResponse response = mapper.toDto(saved);

        log.info("{} OUT : {}", request.couponId(), JacksonUtil.toJson(response));

        URI location = URI.create("/api/user-coupons/" + saved.getId());
        return ResponseEntity.created(location).body(response);
    }
}
