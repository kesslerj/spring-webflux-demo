package org.springframework.demo.webflux;

import java.time.Duration;
import java.time.LocalDateTime;

import reactor.core.publisher.Flux;

/**
 *
 * @author Jonas Keﬂler (jonas.kessler@acando.de)
 */
public class FluxPlayground {

	public static void main(String[] args) throws InterruptedException {

		simpleStringDemo();

		rangeDemo();

		intervalDemo();

	}

	private static void simpleStringDemo() {
		Flux<String> strings = Flux.just("a", "b", "c");

		Flux<String> mapped = strings.map(s -> {
			System.out.println(s);
			return s + s;
		});

		mapped.subscribe();
	}

	private static void rangeDemo() {
		Flux<Integer> range = Flux.range(0, 50);
		range.subscribe(System.out::println);

		Flux<String> transformed = range.map(i -> transform(i));

		System.out.println("Transformation applied, not yet subscribed");

		transformed.subscribe(System.out::println);
	}

	private static String transform(Object input) {
		System.out.println("transforming " + input);
		return "tranformation of " + input;
	}

	private static void intervalDemo() throws InterruptedException {

		LocalDateTime now = LocalDateTime.now();

		Flux<Long> events = Flux.interval(Duration.ofSeconds(1));
		Flux<LocalDateTime> timeStamps = events.map(p -> now.plusSeconds(p));

		timeStamps.subscribe(s -> System.out.println("Aktuelle Uhrzeit: " + s));
		timeStamps.subscribe(i -> System.out.println("Moep"));

		while (true) {
			Thread.sleep(100);
		}
	}

}
