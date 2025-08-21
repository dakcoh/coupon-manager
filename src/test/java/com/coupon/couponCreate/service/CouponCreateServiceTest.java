package com.coupon.couponCreate.service;

import com.coupon.couponCreate.dto.CouponCreateRequest;
import com.coupon.couponCreate.repository.CouponCreateRepository;
import com.coupon.domain.Coupon;
import com.coupon.domain.CouponStatus;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("쿠폰 생성 서비스 테스트")
class CouponCreateServiceTest {

    @InjectMocks
    private CouponCreateService service;

    @Mock
    private CouponCreateRepository repository;

    private CouponCreateRequest validReq;

    @BeforeEach
    void setUp() {
        validReq = new CouponCreateRequest(
                "dakcoh_coupon",
                LocalDateTime.now().plusDays(30),
                BigDecimal.TEN
        );
    }

    @Test
    @DisplayName("정상 생성")
    void create_success() {
        when(repository.save(any(Coupon.class))).thenAnswer(inv -> {
            Coupon c = inv.getArgument(0);
            return Coupon.builder()
                    .id(1L)
                    .couponId(c.getCouponId() != null ? c.getCouponId() : UUID.randomUUID().toString())
                    .couponCode(c.getCouponCode())
                    .createdDate(LocalDateTime.now())
                    .expirationDate(c.getExpirationDate())
                    .discountAmount(c.getDiscountAmount())
                    .status(CouponStatus.AVAILABLE)
                    .build();
        });

        // when
        Coupon saved = service.createCoupon(validReq);

        // then
        assertThat(saved).isNotNull();
        assertThat(saved.getCouponCode()).isEqualTo("dakcoh_coupon");
        assertThat(saved.getDiscountAmount()).isEqualByComparingTo(BigDecimal.TEN);
        assertThat(saved.getExpirationDate()).isAfter(LocalDateTime.now());
        assertThat(saved.getCouponId()).isNotBlank();
        assertThat(saved.getStatus()).isEqualTo(CouponStatus.AVAILABLE);

        verify(repository).save(any(Coupon.class));
        verifyNoMoreInteractions(repository);
    }
}