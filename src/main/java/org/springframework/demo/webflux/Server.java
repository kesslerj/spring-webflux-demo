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

package org.springframework.demo.webflux;

import static org.springframework.web.reactive.function.server.RouterFunctions.toHttpHandler;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.demo.webflux.config.ControllerConfiguration;
import org.springframework.demo.webflux.persistence.DummyPersonRepository;
import org.springframework.demo.webflux.persistence.PersonRepository;
import org.springframework.demo.webflux.rest.routing.PersonHandler;
import org.springframework.demo.webflux.rest.routing.PersonRouter;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.http.server.reactive.ServletHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;

import reactor.ipc.netty.http.server.HttpServer;

public class Server {

	public static final String HOST = "localhost";

	public static final int PORT = 8080;

	public static void main(String[] args) throws Exception {
		Server server = new Server();

		/*
		 * Three different ways to initialize:
		 * - ControllerConfiguration: Spring Context + request processing with PersonController
		 * - RoutingConfiguration: Spring Context + request processing with RouterFunction
		 * - standalone: get HttpHandler directly from RouterFunction, no Spring Context
		 */
		HttpHandler httpHandler = server.applicationContext(ControllerConfiguration.class);
		// HttpHandler httpHandler = server.applicationContext(RoutingConfiguration.class);
		// HttpHandler httpHandler = server.standalone();

		/*
		 * Two different servers to choose
		 * - Reactor Netty
		 * - Tomcat
		 */
		server.startReactorServer(httpHandler);
//		server.startTomcatServer(httpHandler);

		System.out.println("Press ENTER to exit.");
		System.in.read();
	}

	public void startReactorServer(HttpHandler httpHandler) throws InterruptedException {
		ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(httpHandler);
		HttpServer server = HttpServer.create(HOST, PORT);
		server.newHandler(adapter).block();
	}

	public void startTomcatServer(HttpHandler httpHandler) throws LifecycleException {
		Tomcat tomcatServer = new Tomcat();
		tomcatServer.setHostname(HOST);
		tomcatServer.setPort(PORT);
		Context rootContext = tomcatServer.addContext("", System.getProperty("java.io.tmpdir"));
		ServletHttpHandlerAdapter servlet = new ServletHttpHandlerAdapter(httpHandler);
		Tomcat.addServlet(rootContext, "httpHandlerServlet", servlet);
		rootContext.addServletMapping("/", "httpHandlerServlet");
		tomcatServer.start();
	}

	public HttpHandler standalone() {
		PersonRepository repository = new DummyPersonRepository();
		PersonHandler handler = new PersonHandler(repository);

		RouterFunction<?> route = PersonRouter.routerFunction(handler);
		return toHttpHandler(route);
	}

	public HttpHandler applicationContext(Class configurationClass) {
		AnnotationConfigApplicationContext applicationContext =
				new AnnotationConfigApplicationContext(configurationClass);
		return WebHttpHandlerBuilder.applicationContext(applicationContext).build();
	}

}
