package com.igor.n26.service;

import com.igor.n26.model.Statistics;
import com.igor.n26.model.Transaction;
import com.igor.n26.service.clock.Clock;
import com.igor.n26.service.clock.SystemUtcClock;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Calculates real-time transaction statistics.
 *
 * @author Igor Shevchenko
 */
@Service
public class StatisticsService {

    /**
     * Amount of past seconds to consider for computing transaction statistics.
     * Means N-1 seconds before the current second, and the current second.
     */
    static final int LOOKBACK_SECONDS = 60;

    /**
     * Aggregated transaction statistics, on a second-based level.
     */
    private final Statistics[] statistics = new Statistics[LOOKBACK_SECONDS];

    /**
     * Locks for exclusive access to transaction statistics.
     */
    private final Lock[] locks = new Lock[statistics.length];

    /**
     * Corresponding UTC seconds for transaction statistics.
     */
    private final long[] seconds = new long[statistics.length];

    /**
     * Clock for getting current time.
     */
    private final Clock clock;

    public StatisticsService() {
        this(new SystemUtcClock());
    }

    public StatisticsService(Clock clock) {
        this.clock = clock;
        for (int i = 0; i < statistics.length; i++) {
            statistics[i] = new Statistics();
            locks[i] = new ReentrantLock();
            seconds[i] = Long.MIN_VALUE;
        }
    }

    /**
     * Process transaction, and update real-time transaction statistics.
     *
     * @param transaction Transaction.
     * @return false, if transaction is too old (in such case the statistics are not affected).
     */
    public boolean processTransaction(Transaction transaction) {
        long currentSeconds = TimeUnit.MILLISECONDS.toSeconds(clock.currentTimeMillis());
        long transactionSeconds = TimeUnit.MILLISECONDS.toSeconds(transaction.getTimestamp());
        if (currentSeconds - transactionSeconds >= LOOKBACK_SECONDS) {
            return false;
        }

        int statsIndex = (int) (transactionSeconds % LOOKBACK_SECONDS);
        Lock lock = locks[statsIndex];
        try {
            lock.lock();
            updateStatistics(statsIndex, transactionSeconds, transaction.getAmount());
            return true;
        } finally {
            lock.unlock();
        }
    }

    private void updateStatistics(int statsIndex, long transactionSeconds, double transactionAmount) {
        Statistics stats = statistics[statsIndex];
        long statsSeconds = seconds[statsIndex];

        if (statsSeconds != transactionSeconds) {
            seconds[statsIndex] = transactionSeconds;
            statistics[statsIndex] = Statistics.forValue(transactionAmount);
        } else {
            statistics[statsIndex] = stats.merge(transactionAmount);
        }
    }

    /**
     * Get transaction statistics.
     *
     * @return Transaction statistics.
     */
    public Statistics getStatistics() {
        Statistics aggregated = new Statistics();
        long smallestValidSecond = TimeUnit.MILLISECONDS.toSeconds(clock.currentTimeMillis()) - LOOKBACK_SECONDS;
        for (int i = 0; i < statistics.length; i++) {
            Lock lock = locks[i];
            try {
                lock.lock();
                if (seconds[i] > smallestValidSecond) {
                    aggregated = aggregated.merge(statistics[i]);
                }
            } finally {
                lock.unlock();
            }
        }
        return aggregated;
    }

    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
    }

}
