package com.ai.dwsprintreactive.service;


import com.ai.dwsprintreactive.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public interface UserService {

    Flux<User> all();

    Mono<User> get(Integer id);

    Mono<User> getByUuid(UUID uuid);

    Mono<User> update(Integer id, User entity);

    Mono<Void> delete(Integer id);

    Mono<User> create(UUID uuid, String firstName, String lastName, Double weight,
                      Double height, String gender, LocalDate birthday, String username, String email);
}
