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

    @PostMapping("/etl/users")
    public Flux<User> insert() {
        return readWebClient.get()
                .uri("/api/users")
                .retrieve()
                .bodyToFlux(User.class)
                .flatMap(x -> writeWebClient.post()
                        .uri("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue("{" +
                                                      "\"uuid\" : \"" + x.getId() + "\"," +
                                                      "\"firstName\" : \"" + x.getFirstname() + "\"," +
                                                      "\"lastName\" : \"" + x.getLastname() + "\"," +
                                                      "\"weight\" : " + x.getWeight() + "," +
                                                      "\"height\" : " + x.getHeight() + "," +
                                                      "\"gender\" : \"" + x.getGender() + "\"," +
                                                      "\"birthday\" : \"" + x.getBirthday().toLocalDate().toString() + "\"," +
                                                      "\"email\" : \"" + x.getEmail() + "\"," +
                                                      "\"username\" : \"" + x.getUsername() + "\"" +
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
