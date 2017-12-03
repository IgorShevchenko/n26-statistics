package com.igor.n26.service.clock;

/**
 * Clock related functionality.
 *
 * @author Igor Shevchenko
 */
public interface Clock {

    /**
     * Get current time in milliseconds. Measured as difference in milliseconds
     * between the current time and midnight, January 1, 1970 UTC.
     *
     * @return Current time in milliseconds.
     */
    long currentTimeMillis();

}
