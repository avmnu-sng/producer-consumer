package me.avmnusng.counter.controller;

import me.avmnusng.counter.Counter;
import me.avmnusng.counter.worker.*;
import me.avmnusng.counter.entity.Request;
import me.avmnusng.counter.entity.repository.AnalyticsRepository;
import me.avmnusng.counter.entity.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.Resource;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1")
public class CounterController {
    private final RequestRepository requestRepository;
    private final AnalyticsRepository analyticsRepository;

    public CounterController(@Autowired RequestRepository requestRepository, @Autowired AnalyticsRepository analyticsRepository) {
        this.requestRepository = requestRepository;
        this.analyticsRepository = analyticsRepository;
    }

    @Resource(name = "logger")
    Logger logger;

    @Resource(name = "counter")
    Counter counter;

    @Resource(name = "producerFactory")
    WorkerFactory producerFactory;

    @Resource(name = "consumerFactory")
    WorkerFactory consumerFactory;

    /**
     * The endpoint `POST /api/v1/producers` creates a producer thread.
     * The response status is 201 if the producer is created successfully.
     * In the event of any error while processing the request, the response code is 500.
     */
    @PostMapping("/producers")
    @ResponseStatus(HttpStatus.CREATED)
    public void createProducer() {
        try {
            // Create the producer worker
            Worker worker = new ProducerWorker(counter, analyticsRepository);
            // Create the producer thread
            Thread thread = producerFactory.newThread(worker);
            // Persist the request in the database
            requestRepository.save(new Request(WorkerType.PRODUCER.getName(), thread.getName()));
            // Start the thread
            thread.start();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            // Throw any exception with status code 500
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    /**
     * The endpoint `POST /api/v1/consumers` creates a consumer thread.
     * The response status is 201 if the consumer is created successfully.
     * In the event of any error while processing the request, the response code is 500.
     */
    @PostMapping("/consumers")
    @ResponseStatus(HttpStatus.CREATED)
    public void createConsumer() {
        try {
            // Create the consumer worker
            Worker worker = new ConsumerWorker(counter, analyticsRepository);
            // Create the consumer thread
            Thread thread = consumerFactory.newThread(worker);
            // Persist the request in the database
            requestRepository.save(new Request(WorkerType.CONSUMER.getName(), thread.getName()));
            // Start the thread
            thread.start();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            // Throw any exception with status code 500
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    /**
     * The endpoint `PATCH /api/v1/value/{value}` updates the counter value.
     * The response status is 200.
     */
    @PatchMapping("/value/{value}")
    @ResponseStatus(HttpStatus.OK)
    public void setValue(@PathVariable int value) {
        // Update the counter value
        counter.set(value);
    }
}
