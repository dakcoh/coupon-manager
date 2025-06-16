package com.coupon.couponIssue.controller;

import com.coupon.couponCreate.dto.CouponCreateResponse;
import com.coupon.couponIssue.dto.CouponIssueRequest;
import com.coupon.couponIssue.dto.CouponIssueResponse;
import com.coupon.domain.Coupon;
import com.coupon.domain.UserCoupon;
import com.coupon.domain.mapper.CouponIssueMapper;
import com.coupon.facade.CouponFacade;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.util.JacksonUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 쿠폰 지급 관련 API 엔드포인트를 제공하는 컨트롤러입니다.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CouponIssueController {
    private static final Logger log = LoggerFactory.getLogger(CouponIssueController.class);
    private final CouponFacade couponFacade;
    private final CouponIssueMapper couponMapper;

    /**
     * 쿠폰 지급 요청을 처리합니다.
     */
    @PostMapping("/coupons/issue")
    public ResponseEntity<CouponIssueResponse> issueCoupon(@RequestBody CouponIssueRequest request) {
        log.info("IN : {}", JacksonUtil.toJson(request));

        UserCoupon UserCoupon = couponFacade.issueCoupon(request);
        CouponIssueResponse response = couponMapper.toDto(UserCoupon);

        log.info("OUT : {}", JacksonUtil.toJson(response));

        return ResponseEntity.ok(response);
    }
}
