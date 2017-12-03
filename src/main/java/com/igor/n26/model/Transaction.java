package com.igor.n26.model;

/**
 * Transaction.
 *
 * @author Igor Shevchenko
 */
public class Transaction {

    private double amount;
    private long timestamp;

    /**
     * Get transaction amount.
     *
     * @return Transaction amount.
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Set transaction amount.
     *
     * @param amount Transaction amount.
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Get transaction time in epoch millis in UTC time zone.
     *
     * @return Transaction time.
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Set transaction time in epoch millis in UTC time zone.
     *
     * @param timestamp Transaction time.
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}
