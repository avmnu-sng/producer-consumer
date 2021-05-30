package me.avmnusng.counter.worker;

/**
 * The two workers `producer` and `consumer`.
 */
public enum WorkerType {
    PRODUCER("producer"),
    CONSUMER("consumer");

    private final String name;

    public String getName() {
        return name;
    }

    WorkerType(String name) {
        this.name = name;
    }
}
