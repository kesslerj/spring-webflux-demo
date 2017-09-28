package org.springframework.demo.webflux.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.demo.webflux.persistence.DummyPersonRepository;
import org.springframework.demo.webflux.persistence.PersonRepository;
import org.springframework.demo.webflux.rest.controller.PersonController;
import org.springframework.web.reactive.config.EnableWebFlux;

/**
 *
 * @author Jonas Keﬂler (jonas.kessler@acando.de)
 */
@Configuration
@EnableWebFlux
public class WebFluxOnlyConfiguration {

	@Bean
	public PersonRepository repository() {
		return new DummyPersonRepository();
	}

	@Bean
	public PersonController controller(PersonRepository repository) {
		return new PersonController(repository);
	}

}
