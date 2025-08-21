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
    private Long id;

    @Column(nullable = false)
    private String couponId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private LocalDateTime issuedDate; // 발급일

    @Column(nullable = false)
    private LocalDateTime expirationDate; // 만료일

    private LocalDateTime usedDate; // 사용일

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=16)
    private CouponStatus status;

    @PrePersist
    void onCreate() {
        if (issuedDate == null) issuedDate = LocalDateTime.now();
        if (status == null) status = CouponStatus.AVAILABLE;
        if (!expirationDate.isAfter(issuedDate)) throw new IllegalStateException("만료일은 발급일 이후여야 합니다.");
    }

    public boolean isExpiredNow() {
        return LocalDateTime.now().isAfter(expirationDate);
    }

    /* 사용 처리: 상태/만료 가드 포함 */
    public void use() {
        if (status != CouponStatus.AVAILABLE) throw new IllegalStateException("사용 불가 상태");
        if (isExpiredNow()) {
            status = CouponStatus.EXPIRED;
            throw new IllegalStateException("만료된 쿠폰");
        }
        transit(CouponStatus.USED);
    }

    /* 만료 처리: AVAILABLE에만 적용, 멱등 */
    public void expire() {
        if (status == CouponStatus.AVAILABLE) status = CouponStatus.EXPIRED;
    }

    /* 전이 공통(규칙 위반 시 예외) */
    public void transit(CouponStatus target) {
        this.status.validateTransition(target);
        this.status = target;
        if (target == CouponStatus.USED) this.usedDate = LocalDateTime.now();
    }

    /** 팩토리: Coupon → UserCoupon */
    public static UserCoupon issueFrom(Coupon coupon, String userId) {
        coupon.ensureIssuable(); // 쿠폰 도메인에서 만료/상태 검사
        return UserCoupon.builder()
                .couponId(coupon.getCouponId())
                .userId(userId)
                .issuedDate(LocalDateTime.now())
                .expirationDate(coupon.getExpirationDate())
                .status(CouponStatus.AVAILABLE)
                .build();
    }
}
