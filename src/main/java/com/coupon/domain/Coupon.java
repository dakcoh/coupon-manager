package com.coupon.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

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
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "coupon_id", nullable = false, unique = true)
    private String couponId;

    @Column(name = "coupon_code", nullable = false, unique = true)
    private String couponCode;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;

    @Column(name = "discount_amount")
    private double discountAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CouponStatus status;

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }

    /**
     * 쿠폰의 유효성을 검사
     * 쿠폰이 사용 가능 상태이며 아직 만료되지 않았는지를 확인합니다.
     * 만약 만료되었다면 상태를 EXPIRED("0003")로 업데이트합니다.
     */
    public boolean isValid() {
        if (!this.status.equals(CouponStatus.fromCode("0001"))) {
            return false;
        }
        if (LocalDateTime.now().isAfter(expirationDate)) {
            this.status = CouponStatus.fromCode("0003");
            return false;
        }
        return true;
    }
}
