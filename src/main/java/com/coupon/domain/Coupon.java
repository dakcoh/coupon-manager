package com.coupon.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "coupons")
@Getter
@ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String couponId;

    @Column(nullable = false, unique = true)
    private String couponCode;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @Column(nullable = false)
    private LocalDateTime expirationDate;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal discountAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private CouponStatus status;

    @PrePersist
    protected void onCreate() {
        if (createdDate == null) createdDate = LocalDateTime.now();
        if (couponId == null) couponId = UUID.randomUUID().toString();
        if (status == null) status = CouponStatus.AVAILABLE;
    }

    public static Coupon create(String code, LocalDateTime expirationDate, BigDecimal discountAmount) {
        if (code == null || code.isBlank())
            throw new IllegalArgumentException("쿠폰 코드는 필수입니다.");
        if (expirationDate == null || !expirationDate.isAfter(LocalDateTime.now()))
            throw new IllegalArgumentException("만료일은 현재 시각 이후여야 합니다.");
        if (discountAmount == null || discountAmount.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("할인 금액은 0 이상이어야 합니다.");

        return Coupon.builder()
                .couponCode(code)
                .expirationDate(expirationDate)
                .discountAmount(discountAmount)
                .status(CouponStatus.AVAILABLE)
                .build();
    }
}
