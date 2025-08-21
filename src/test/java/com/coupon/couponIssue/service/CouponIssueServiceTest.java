package com.coupon.couponIssue.service;

import com.coupon.domain.CouponStatus;
import com.coupon.domain.UserCoupon;
import com.coupon.facade.CouponFacade;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("쿠폰 발급 동시성 테스트")
class CouponIssueServiceTest {

    private static final Logger log = LoggerFactory.getLogger(CouponIssueServiceTest.class);

    @InjectMocks
    private CouponFacade facade;                 // 퍼사드 실제 객체(의존성 주입 대상)

    @Mock
    private CouponIssueService issueService;     // 퍼사드가 위임할 서비스 Mock

    private static final String COUPON_ID = UUID.randomUUID().toString();

    @BeforeEach
    void stubService() {
        when(issueService.issueCoupon(anyString(), anyString()))
                .thenAnswer(inv -> UserCoupon.builder()
                        .id(ThreadLocalRandom.current().nextLong(1, 1_000_000))
                        .couponId(inv.getArgument(0, String.class))
                        .userId(inv.getArgument(1, String.class))
                        .issuedDate(LocalDateTime.now())
                        .expirationDate(LocalDateTime.now().plusDays(30))
                        .status(CouponStatus.AVAILABLE)
                        .build());
    }

    @Test
    @DisplayName("1000개 동시 요청 처리")
    void concurrent_issue_requests() throws Exception {
        int threads = 1000;
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        CountDownLatch start = new CountDownLatch(1);
        List<Future<UserCoupon>> futures = new ArrayList<>(threads);

        for (int i = 0; i < threads; i++) {
            final String userId = "user" + i;
            futures.add(pool.submit(() -> {
                start.await();
                return facade.issueCoupon(COUPON_ID, userId); // 퍼사드 호출
            }));
        }
        start.countDown();

        int success = 0;
        for (Future<UserCoupon> f : futures) {
            try {
                if (f.get(5, TimeUnit.SECONDS) != null) success++;
            } catch (TimeoutException e) {
                log.warn("timeout");
            }
        }
        pool.shutdown();
        assertThat(success).isEqualTo(threads);

        verify(issueService, times(threads)).issueCoupon(eq(COUPON_ID), anyString());
        verifyNoMoreInteractions(issueService);
    }
}
