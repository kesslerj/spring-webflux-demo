/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 * May be modified, original class by Arjen Poutsma : https://github.com/poutsma/webflux-functional-demo
 */

package org.springframework.demo.webflux.rest.routing;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.demo.webflux.persistence.Person;
import org.springframework.demo.webflux.persistence.PersonRepository;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class PersonHandler {

	private static final Log LOGGER = LogFactory.getLog(PersonHandler.class);

	private final PersonRepository repository;

	public PersonHandler(PersonRepository repository) {
		this.repository = repository;
	}

	public Mono<ServerResponse> getPerson(ServerRequest request) {
		Mono<Integer> personId = Mono.just(Integer.valueOf(request.pathVariable("id")));
		Mono<Person> personMono = this.repository.getPerson(personId);
		LOGGER.info("getPerson - after calling repository");
		Mono<ServerResponse> response = personMono
				.flatMap(person -> ServerResponse.ok().contentType(APPLICATION_JSON).body(fromObject(person)))
				.switchIfEmpty(ServerResponse.notFound().build());
		LOGGER.info("getPerson - after creating response");
		return response;
	}

	public Mono<ServerResponse> savePerson(ServerRequest request) {
		Mono<Person> person = request.bodyToMono(Person.class);
		Mono<Void> savePerson = this.repository.savePerson(person);
		LOGGER.info("savePerson - after calling repository");
		Mono<ServerResponse> response = ServerResponse.ok().build(savePerson);
		LOGGER.info("savePerson - after creating response");
		return response;
	}

	public Mono<ServerResponse> allPeople(ServerRequest request) {
		Flux<Person> people = this.repository.allPeople();
		LOGGER.info("allPeople - after calling repository");
		Mono<ServerResponse> response = ServerResponse.ok().contentType(APPLICATION_JSON).body(people, Person.class);
		LOGGER.info("allPeople - after creating response");
		return response;
	}

}
