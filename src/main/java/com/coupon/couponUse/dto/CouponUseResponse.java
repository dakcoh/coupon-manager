package com.coupon.couponUse.dto;

import com.coupon.domain.CouponStatus;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 쿠폰 사용 결과를 클라이언트에 반환하기 위한 응답 DTO입니다.
 */
@Data
@Builder
public class CouponUseResponse {
    private String couponId;
    private String couponCode;
    private LocalDateTime usedDate;
    private CouponStatus status;
}
