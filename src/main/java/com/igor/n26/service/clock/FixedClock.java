package com.igor.n26.service.clock;

/**
 * Fixed clock. Always returns the explicitly specified time.
 *
 * @author Igor Shevchenko
 */
public class FixedClock implements Clock {

    private long millis;

    public void setMillis(long millis) {
        this.millis = millis;
    }

    @Override
    public long currentTimeMillis() {
        return millis;
    }

}

