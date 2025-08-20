package com.coupon.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_coupons")
@Data
@ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "coupon_id", nullable = false)
    private String couponId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "issued_date", nullable = false)
    private LocalDateTime issuedDate; // 발급일

    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate; // 만료일

    @Column(name = "used_date")
    private LocalDateTime usedDate; // 사용일

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CouponStatus status;

    public void transit(CouponStatus target) {
        this.status.validateTransition(target);
        this.status = target;

        if(target == CouponStatus.USED) {
            this.usedDate = LocalDateTime.now();
        }
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expirationDate);
    }

    @PrePersist
    protected void onIssue() {
        this.issuedDate = LocalDateTime.now();
        this.status = CouponStatus.fromCode("0001"); // 기본값: 사용 가능(AVAILABLE)
    }
}
