package com.ai.etl.controller;

import com.ai.etl.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@RestController
@Slf4j
public class UserEtl {

    WebClient readWebClient = WebClient.create("http://localhost:8090");
    WebClient writeWebClient = WebClient.create("http://localhost:8080");

    private final static MediaType json = MediaType.APPLICATION_JSON;

    @PostMapping("/etl/users")
    public Flux<User> insert() {
        return readWebClient.get()
                .uri("/api/users")
                .retrieve()
                .bodyToFlux(User.class)
                .flatMap(user -> writeWebClient.post()
                        .uri("/api/users")
                        .contentType(json)
                        .body(BodyInserters.fromValue("{" +
                                                      "\"uuid\":\"" + user.getId() + "\"," +
                                                      "\"firstName\":\"" + user.getFirstname() + "\"," +
                                                      "\"lastName\":\"" + user.getLastname() + "\"," +
                                                      "\"weight\":" + user.getWeight() + "," +
                                                      "\"height\":" + user.getHeight() + "," +
                                                      "\"gender\":\"" + user.getGender() + "\"," +
                                                      "\"birthday\":\"" + user.getBirthday().toLocalDate().toString() + "\"," +
                                                      "\"email\":\"" + user.getEmail() + "\"," +
                                                      "\"username\":\"" + user.getUsername() + "\"" +
                                                      "}"))
                        .exchange()
                        .flatMap(clientResponse -> {
                            if (clientResponse.statusCode().is5xxServerError()) {
                                clientResponse.body((clientHttpResponse, context) -> clientHttpResponse.getBody());
                            }
                            return clientResponse.bodyToMono(User.class);
                        }));
    }
}
