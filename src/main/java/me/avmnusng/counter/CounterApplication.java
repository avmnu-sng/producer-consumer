package me.avmnusng.counter;

import me.avmnusng.counter.worker.WorkerFactory;
import me.avmnusng.counter.worker.WorkerType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.logging.Logger;

@SpringBootApplication
public class CounterApplication {
	public static void main(String[] args) {
		SpringApplication.run(CounterApplication.class, args);
	}

	/**
	 *
	 * The logger to provide the logging interface
	 */
	@Bean(name = "logger")
	public Logger logger() {
		return Logger.getLogger(getClass().getName());
	}

	/**
	 *
	 * The counter instance holding the value
	 */
	@Bean(name = "counter")
	public Counter counter() {
		return new Counter();
	}

	/**
	 *
	 * The producer thread factory to create producer threads
	 */
	@Bean(name = "producerFactory")
	public WorkerFactory producerFactory() {
		return new WorkerFactory(WorkerType.PRODUCER);
	}

	/**
	 *
	 * The consumer thread factory to create consumer threads
	 */
	@Bean(name = "consumerFactory")
	public WorkerFactory consumerFactory() {
		return new WorkerFactory(WorkerType.CONSUMER);
	}
}
