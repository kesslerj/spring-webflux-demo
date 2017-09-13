package org.springframework.demo.webflux.rest.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.demo.webflux.persistence.Person;
import org.springframework.demo.webflux.persistence.PersonRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/persons")
public class PersonController {

	private static final Log LOGGER = LogFactory.getLog(PersonController.class);

	private final PersonRepository repository;

	public PersonController(PersonRepository repository) {
		this.repository = repository;
	}

	@GetMapping
	public Flux<Person> allPeople() {
		// TODO: not sure if correct, compare comment in repo method

		LOGGER.info("GET /persons incoming");
		Flux<Person> allPeople = this.repository.allPeople();
		LOGGER.info("GET /persons returning");
		return allPeople;
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public Mono<Void> savePerson(@RequestBody Mono<Person> personMono) {
		LOGGER.info("POST /persons incoming");
		Mono<Void> mono = this.repository.savePerson(personMono);
		LOGGER.info("POST /persons returning");
		return mono;
	}

	@GetMapping("/{id}")
	public Mono<Person> getPerson(@PathVariable Integer id) {
		// imho pathvariable lacks reactiveness, as it is not wrapped in a reactive type like Requestbody above
		// but very probably because otherwise the mapping to this method is not possible

		LOGGER.info("GET /persons/{id} incoming");
		Mono<Person> mono = this.repository.getPerson(Mono.justOrEmpty(id))
				.switchIfEmpty(Mono.error(new NotFoundException()));
		LOGGER.info("GET /persons/{id} returning");
		return mono;
	}

	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public void handle() {

	}

}
