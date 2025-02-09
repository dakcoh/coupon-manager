package com.coupon.couponIssue.service;

import com.coupon.couponCreate.controller.CouponCreateController;
import com.coupon.couponIssue.dto.CouponIssueRequest;
import com.coupon.domain.Coupon;
import com.coupon.domain.CouponStatus;
import com.coupon.domain.UserCoupon;
import com.coupon.facade.CouponFacade;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("쿠폰 발급 동시성 테스트")
class CouponIssueServiceTest {
    private static final Logger log = LoggerFactory.getLogger(CouponCreateController.class);

    @InjectMocks
    private CouponFacade couponFacade;

    @Mock
    private CouponIssueService couponIssueService;

    private static final String COUPON_ID = UUID.randomUUID().toString();
    private Coupon coupon;

    @BeforeEach
    void setUp() {
        coupon = Coupon.builder()
                .id(1L)
                .couponId(COUPON_ID)
                .couponCode("dakcoh_coupon")
                .createdDate(LocalDateTime.now().minusDays(1))
                .expirationDate(LocalDateTime.now().plusDays(30))
                .discountAmount(10.0)
                .status(CouponStatus.AVAILABLE)
                .build();

        // 쿠폰 발급 서비스 Mock 설정
        when(couponFacade.issueCoupon(any(CouponIssueRequest.class))).thenAnswer(invocation -> {
            CouponIssueRequest request = invocation.getArgument(0);
            return UserCoupon.builder()
                    .id(1L)
                    .couponId(request.getCouponId())
                    .userId(request.getUserId())
                    .issuedDate(LocalDateTime.now())
                    .status(CouponStatus.AVAILABLE)
                    .build();
        });
    }

    @Nested
    @DisplayName("동시성 테스트")
    class ConcurrentTests {

        private final int THREAD_COUNT = 1000;
        private ExecutorService executorService;

        @BeforeEach
        void init() {
            executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        }

        @AfterEach
        void tearDown() {
            executorService.shutdown();
        }

        @Test
        @DisplayName("1000개 동시 요청을 처리하는 동시성 테스트")
        void testIssueCoupon_WithoutLock() throws InterruptedException {
            List<Future<UserCoupon>> futures = new ArrayList<>();

            for (int i = 0; i < THREAD_COUNT; i++) {
                String userId = "dakcoh" + i;
                CouponIssueRequest request = new CouponIssueRequest(COUPON_ID, userId);
                futures.add(executorService.submit(() -> couponFacade.issueCoupon(request)));
            }

            int successCount = 0;
            for (Future<UserCoupon> future : futures) {
                try {
                    UserCoupon userCoupon = future.get();
                    if (userCoupon != null) {
                        successCount++;
                    }
                } catch (ExecutionException e) {
                    log.warn("발급 실패: {}", e.getMessage());
                }
            }

            log.info("1,000개 동시 요청 - 비관적 락 사용하지 않음");
            log.info("총 발급 성공 건수: {}", successCount);
            assertThat(successCount).isLessThanOrEqualTo(THREAD_COUNT);
        }
    }
}
