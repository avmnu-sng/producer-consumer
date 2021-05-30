package me.avmnusng.counter;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * The counter shared amongst producer and consumer threads.
 */
public class Counter {
    @Resource(name = "logger")
    Logger logger;

    // The default value is 50
    public static final int DEFAULT_VALUE = 50;
    // The lower limit is 0
    public static final int LOWER_LIMIT = 0;
    // The upper limit is 100
    public static final int UPPER_LIMIT = 100;
    // The thread-safe counter value
    private final AtomicInteger value;

    public Counter() {
        // Set the initial value
        this.value = new AtomicInteger(DEFAULT_VALUE);
    }

    public int get() {
        // Return the current value
        return value.get();
    }

    public void set(int newValue) {
        // Update the value
        value.set(newValue);
    }

    public void increase() {
        // Increase the value by 1
        logger.info(Thread.currentThread().getName() + " incremented value to " + value.incrementAndGet());
    }

    public void decrease() {
        // Decrease the value by 1
        logger.info(Thread.currentThread().getName() + " decremented value to " + value.decrementAndGet());
    }
}
