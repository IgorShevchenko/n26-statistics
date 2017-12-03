package com.igor.n26.service;

import com.igor.n26.model.Statistics;
import com.igor.n26.model.Transaction;
import com.igor.n26.service.clock.FixedClock;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * Test cases for {@link StatisticsService}.
 *
 * @author Igor Shevchenko
 */
public class StatisticsServiceTest {

    private final FixedClock testClock = new FixedClock();
    private final StatisticsService service = new StatisticsService(testClock);

    @Test
    public void shouldComputeStatistics() {
        double amount = 10;
        double expectedSum = 0;
        int expectedCount = 0;

        testClock.setMillis(System.currentTimeMillis());
        long to = testClock.currentTimeMillis();
        long from = to - TimeUnit.SECONDS.toMillis(StatisticsService.LOOKBACK_SECONDS - 1);

        for (long timestamp = from; timestamp <= to; timestamp = timestamp + 1000) {
            service.processTransaction(newTransaction(amount, timestamp));
            expectedSum += amount++;
            expectedCount++;
        }

        Statistics statistics = service.getStatistics();
        Assertions.assertThat(statistics.getCount()).isEqualTo(expectedCount).isEqualTo(StatisticsService.LOOKBACK_SECONDS);
        Assertions.assertThat(statistics.getSum()).isEqualTo(expectedSum);
    }

    @Test
    public void shouldIgnoreOldStatistics() {
        testClock.setMillis(System.currentTimeMillis());
        long to = testClock.currentTimeMillis();
        long from = to - TimeUnit.SECONDS.toMillis(StatisticsService.LOOKBACK_SECONDS - 1);

        for (long timestamp = from; timestamp <= to; timestamp = timestamp + 1000) {
            service.processTransaction(newTransaction(10, timestamp));
        }

        // Forward clock by 5 seconds
        testClock.setMillis(testClock.currentTimeMillis() + 5000);
        Statistics statistics = service.getStatistics();
        Assertions.assertThat(statistics.getCount()).isEqualTo(StatisticsService.LOOKBACK_SECONDS - 5);
        Assertions.assertThat(statistics.getSum()).isEqualTo((StatisticsService.LOOKBACK_SECONDS - 5) * 10);

        // Forward clock far to the future
        testClock.setMillis(testClock.currentTimeMillis() + StatisticsService.LOOKBACK_SECONDS * 2000);
        statistics = service.getStatistics();
        Assertions.assertThat(statistics.getCount()).isZero();
        Assertions.assertThat(statistics.getSum()).isZero();
    }

    @Test
    public void shouldAcceptTransactions() {
        testClock.setMillis(System.currentTimeMillis());
        long validTime = testClock.currentTimeMillis();
        long invalidTime = validTime - StatisticsService.LOOKBACK_SECONDS * 1000;
        Assertions.assertThat(service.processTransaction(newTransaction(10, validTime))).isTrue();
        Assertions.assertThat(service.processTransaction(newTransaction(10, invalidTime))).isFalse();
    }

    private Transaction newTransaction(double amount, long timestamp) {
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setTimestamp(timestamp);
        return transaction;
    }

}
