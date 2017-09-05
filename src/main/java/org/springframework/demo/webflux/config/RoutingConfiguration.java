package org.springframework.demo.webflux.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.demo.webflux.persistence.DummyPersonRepository;
import org.springframework.demo.webflux.persistence.PersonRepository;
import org.springframework.demo.webflux.rest.routing.PersonHandler;
import org.springframework.demo.webflux.rest.routing.PersonRouter;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * @author Arjen Poutsma
 */
@Configuration
@EnableWebFlux
public class RoutingConfiguration {

	@Bean
	public PersonRepository repository() {
		return new DummyPersonRepository();
	}

	@Bean
	public PersonHandler handler(PersonRepository repository) {
		return new PersonHandler(repository);
	}

	@Bean
	public RouterFunction<ServerResponse> routerFunction(PersonHandler handler) {
		return PersonRouter.routerFunction(handler);
	}

}
