# Spring 5 Functional Web Framework Sample

This repository contains a sample application that uses the functional web framework introduced in Spring 5.
It mainly consists of the following types:

| Class                   | Description                                   |
| ----------------------- | --------------------------------------------- |
| `Person`                | POJO representing a person                    |
| `PersonRepository`      | Reactive repository for `Person`              |
| `DummyPersonRepository` | Dummy implementation of `PersonRepository`    |
| `PersonHandler`         | Web handler that exposes a `PersonRepository` |
| `PersonRouter`          | Provides a functional `RouterFunction`        |
| `PersonController`       | A reactive `RestController`                   |
| `Server`                | Contains a `main` method to start the server  |

### Spring Context - or not
In the main method in `Server` there are four ways to start the application:
1. __`ControllerConfiguration`__: Spring Context + request processing with `PersonController`
2. __`RoutingConfiguration`__: Spring Context + request processing with `RouterFunction`
3. __'functionalBeanRegistration`__: Same as first one, but with new programmatic way to register beans
4. __standalone__: get HttpHandler directly from RouterFunction, no Spring Context

Additionally you can choose with what server to start, see the next two points.

### Running the Reactor Netty server
 - Build using maven
 - Run the `org.springframework.samples.web.reactive.function.Server` class
 
### Running the Tomcat server
 - Comment out the `startReactorServer()` line in `Server.java`
 - Uncomment the `startTomcatServer()` line in `Server.java`
 - Build using maven
 - Run the `org.springframework.samples.web.reactive.function.Server` class

### Sample curl commands

Instead of using a Rest-Client of your choice, here are some sample `curl` commands that access resources exposed
by this sample:

```sh
curl -v 'http://localhost:8080/person'
curl -v 'http://localhost:8080/person/1'
curl -d '{"name":"Jack Doe","age":"16"}' -H 'Content-Type: application/json' -v 'http://localhost:8080/person'
```

### Copyright
This project was forked from https://github.com/poutsma/webflux-functional-demo by Arjen Poutsma.
The original project is released under version 2.0 of the [Apache License][].

[Apache License]: http://www.apache.org/licenses/LICENSE-2.0
