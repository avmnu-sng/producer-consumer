package me.avmnusng.counter.worker;

import java.util.UUID;
import java.util.concurrent.ThreadFactory;

/**
 * The worker factory to create the threads with a give name
 */
public class WorkerFactory implements ThreadFactory {
    private final WorkerType workerType;

    public WorkerFactory(WorkerType workerType) {
        this.workerType = workerType;
    }

    @Override
    public Thread newThread(Runnable r) {
        // Thread unique id
        String uniqueId = UUID.randomUUID().toString();
        // Thread name prefixed with the worker type
        String threadName = workerType.getName() + "-" + uniqueId;
        // Create the thread with the above name
        return new Thread(r, threadName);
    }
}
