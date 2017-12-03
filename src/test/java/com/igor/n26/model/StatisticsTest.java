package com.igor.n26.model;

import org.assertj.core.api.Assertions;
import org.junit.Test;

/**
 * Test cases for {@link Statistics}.
 *
 * @author Igor Shevchenko
 */
public class StatisticsTest {

    @Test
    public void shouldBeInitializedWithCorrectValues() {
        Statistics stats = new Statistics();
        Assertions.assertThat(stats.getAvg()).isZero();
        Assertions.assertThat(stats.getSum()).isZero();
        Assertions.assertThat(stats.getCount()).isZero();
        Assertions.assertThat(stats.getMax()).isEqualTo(Double.MIN_VALUE);
        Assertions.assertThat(stats.getMin()).isEqualTo(Double.MAX_VALUE);
    }

    @Test
    public void shouldMergeStatistics() {
        Statistics statsA = new Statistics();
        Statistics statsB = new Statistics();

        statsA = statsA.merge(20);
        statsB = statsB.merge(30);
        Statistics merged = statsA.merge(statsB);

        Assertions.assertThat(merged.getCount()).isEqualTo(2);
        Assertions.assertThat(merged.getAvg()).isEqualTo(25);
        Assertions.assertThat(merged.getMax()).isEqualTo(30);
        Assertions.assertThat(merged.getMin()).isEqualTo(20);
        Assertions.assertThat(merged.getSum()).isEqualTo(50);
    }

}
