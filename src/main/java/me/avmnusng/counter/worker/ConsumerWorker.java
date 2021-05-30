package me.avmnusng.counter.worker;

import me.avmnusng.counter.Counter;
import me.avmnusng.counter.entity.Analytics;
import me.avmnusng.counter.entity.repository.AnalyticsRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The consumer worker inherits from the worker
 */
public class ConsumerWorker extends Worker {
    public ConsumerWorker(Counter counter, @Autowired AnalyticsRepository analyticsRepository) {
        super(counter, WorkerType.CONSUMER, analyticsRepository);
    }

    private void updateCounter() {
        // Decrease the counter value by 1
        counter.decrease();

        if (counterAtBoundary()) {
            // Persist the event in the `analytics` table
            analyticsRepository.save(new Analytics(WorkerType.CONSUMER.getName(), Thread.currentThread().getName(), counter.get()));
        }
    }

    @Override
    public void run() {
        // Run forever
        while (true) {
            // Counter value reached to either lower limit or upper limit updated by some other worker thread
            if (counterAtBoundary()) {
                break;
            }

            updateCounter();

            // Counter value reached to either lower limit or upper limit updated by this worker thread
            if (counterAtBoundary()) {
                break;
            }

            busyWait();
        }
    }
}
