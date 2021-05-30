package me.avmnusng.counter;

import me.avmnusng.counter.worker.WorkerFactory;
import me.avmnusng.counter.worker.WorkerType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class CounterApplicationTests {
	@Resource(name = "counter")
	Counter counter;

	@Resource(name = "counter")
	Counter anotherCounter;

	@Resource(name = "producerFactory")
	WorkerFactory producerFactory;

	@Resource(name = "producerFactory")
	WorkerFactory anotherProducerFactory;

	@Resource(name = "consumerFactory")
	WorkerFactory consumerFactory;

	@Resource(name = "consumerFactory")
	WorkerFactory anotherConsumerFactory;

	@Test
	void sharedCounter() {
		assertEquals(counter, anotherCounter);
		assertEquals(counter.get(), anotherCounter.get());
	}

	@Test
	void counterDefaultValue() {
		assertEquals(counter.get(), Counter.DEFAULT_VALUE);
	}

	@Test
	void sharedCounterAfterIncrement() {
		counter.increase();

		assertEquals(counter.get(), anotherCounter.get());
	}

	@Test
	void sharedCounterAfterDecrement() {
		counter.decrease();

		assertEquals(counter.get(), anotherCounter.get());
	}

	@Test
	void producerFactorySingletonIoC() {
		assertEquals(producerFactory, anotherProducerFactory);
	}

	@Test
	void producerThreadName() {
		assertTrue(producerFactory.newThread(() -> {}).getName().startsWith(WorkerType.PRODUCER.getName()));
	}

	@Test
	void producerThreadState() {
		assertEquals(producerFactory.newThread(() -> {}).getState(), Thread.State.NEW);
	}

	@Test
	void consumerFactorySingletonIoC() {
		assertEquals(consumerFactory, anotherConsumerFactory);
	}

	@Test
	void consumerThreadName() {
		assertTrue(consumerFactory.newThread(() -> {}).getName().startsWith(WorkerType.CONSUMER.getName()));
	}

	@Test
	void consumerThreadState() {
		assertEquals(consumerFactory.newThread(() -> {}).getState(), Thread.State.NEW);
	}
}
