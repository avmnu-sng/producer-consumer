package me.avmnusng.counter.worker;

import me.avmnusng.counter.Counter;
import me.avmnusng.counter.entity.repository.AnalyticsRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.logging.Logger;

/**
 * The worker class responsible for updating the counter value. It has the following life-cycle:
 * - At the start, it checks for the stop condition, i.e., the counter value is equal to the lower or upper limit
 * - Next, being a producer worker, it will increment the value and decrement the value otherwise.
 * - Again, check for the stop condition and persist the event in the `analytics` table also.
 *
 * The worker stops only when the counter value is at the lower limit or the upper limit.
 */
public abstract class Worker implements Runnable {
    @Resource(name = "logger")
    Logger logger;

    protected final Counter counter;
    protected final WorkerType workerType;
    protected final AnalyticsRepository analyticsRepository;

    public Worker(Counter counter, WorkerType workerType, @Autowired AnalyticsRepository analyticsRepository) {
        this.counter = counter;
        this.workerType = workerType;
        this.analyticsRepository = analyticsRepository;
    }

    protected void busyWait() {
        String appEnvironment = System.getenv("APP_ENV");

        if (appEnvironment == null || !appEnvironment.equals("development")) {
            return;
        }

        try {
            // For the development environment add 1 second sleep to be able to monitor the logs and make API calls.
            Thread.sleep(2_000);
        } catch (InterruptedException e) {
            logger.warning(e.getMessage());
        }
    }

    protected boolean counterAtBoundary() {
        // Fetch the current value
        int currentValue = counter.get();

        // Check for the boundary values
        return currentValue == Counter.LOWER_LIMIT || currentValue == Counter.UPPER_LIMIT;
    }

    @Override
    public void run() {
        // Each worker has respective run definition
    }
}
