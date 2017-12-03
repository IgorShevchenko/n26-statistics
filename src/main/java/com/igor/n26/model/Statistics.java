package com.igor.n26.model;

/**
 * Immutable statistics managing count, max/min and sum over the passed values.
 *
 * @author Igor Shevchenko
 */
public final class Statistics {

    private double count = 0;
    private double max = Double.MIN_VALUE;
    private double min = Double.MAX_VALUE;
    private double sum = 0;

    public double getCount() {
        return count;
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }

    public double getSum() {
        return sum;
    }

    public double getAvg() {
        if (count == 0) {
            return 0;
        }
        return sum / count;
    }

    public Statistics merge(double value) {
        Statistics merged = new Statistics();
        merged.count = count + 1;
        merged.max = Math.max(max, value);
        merged.min = Math.min(min, value);
        merged.sum = sum + value;
        return merged;
    }

    public Statistics merge(Statistics other) {
        Statistics merged = new Statistics();
        merged.count = count + other.count;
        merged.max = Math.max(max, other.max);
        merged.min = Math.min(min, other.min);
        merged.sum = sum + other.sum;
        return merged;
    }

    public static Statistics forValue(double value) {
        Statistics stats = new Statistics();
        stats.count = 1;
        stats.max = value;
        stats.min = value;
        stats.sum = value;
        return stats;
    }

}
