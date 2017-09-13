package org.springframework.demo.webflux.rest.routing;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;
import static org.springframework.web.reactive.function.server.RequestPredicates.method;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 *
 * @author Jonas Keﬂler (jonas.kessler@acando.de)
 */
public class PersonRouter {

	public static RouterFunction<ServerResponse> routerFunction(PersonHandler handler) {
		return nest(path("/persons"),
				nest(accept(APPLICATION_JSON),
						route(GET("/{id}"), handler::getPerson)
								.andRoute(method(HttpMethod.GET), handler::allPeople))
										.andRoute(POST("/").and(contentType(APPLICATION_JSON)), handler::savePerson));
	}

}
