package com.igor.n26.service.clock;

/**
 * System UTC clock. Always returns the time of the host machine.
 *
 * @author Igor Shevchenko
 */
public class SystemUtcClock implements Clock {

    private final java.time.Clock systemUtc = java.time.Clock.systemUTC();

    @Override
    public long currentTimeMillis() {
        return systemUtc.millis();
    }

}
