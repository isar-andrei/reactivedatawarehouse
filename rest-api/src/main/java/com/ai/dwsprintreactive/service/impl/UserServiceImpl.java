package com.ai.dwsprintreactive.service.impl;

import com.ai.dwsprintreactive.model.User;
import com.ai.dwsprintreactive.repository.UserRepository;
import com.ai.dwsprintreactive.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component("UserService")
public class UserServiceImpl implements UserService {

    @NotNull private final UserRepository repository;

    @Override
    public Flux<User> all() {
        return repository.findAll();
    }

    @Override
    public Mono<User> get(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Mono<User> getByUuid(UUID uuid) {
        return repository.findUserByUuid(uuid);
    }

    @Override
    public Mono<User> update(Integer id, User entity) {
        return get(id)
                .map(x -> {
                    Integer userId = x.getId();
                    UUID uuid = x.getUuid();
                    String firstName = entity.getFirstName() != null ? entity.getFirstName() : x.getFirstName();
                    String lastName = entity.getLastName() != null ? entity.getLastName() : x.getLastName();
                    Double weight = entity.getWeight() != null ? entity.getWeight() : x.getWeight();
                    Double height = entity.getHeight() != null ? entity.getHeight() : x.getHeight();
                    String gender = entity.getGender() != null ? entity.getGender() : x.getGender();
                    LocalDate birthday = entity.getBirthday() != null ? entity.getBirthday() : x.getBirthday();
                    String username = entity.getUsername() != null ? entity.getUsername() : x.getUsername();
                    String email = entity.getEmail()!= null ? entity.getEmail() : x.getEmail();
                    LocalDateTime createdAt = entity.getCreatedAt() != null ? entity.getCreatedAt() : x.getCreatedAt();

                    return User.builder()
                            .id(userId).uuid(uuid).firstName(firstName).lastName(lastName)
                            .weight(weight).height(height).gender(gender).birthday(birthday)
                            .username(username).email(email).createdAt(createdAt)
                            .build();
                })
                .flatMap(repository::save);
    }

    @Override
    public Mono<Void> delete(Integer id) {
        return repository
                .findById(id)
                .flatMap(f -> repository.deleteById(f.getId()));
    }

    @Override
    public Mono<User> create(UUID uuid, String firstName, String lastName, Double weight, Double height,
                             String gender, LocalDate birthday, String username, String email) {

        User entity = User.builder()
                .uuid(uuid).firstName(firstName).lastName(lastName)
                .weight(weight).height(height).gender(gender)
                .birthday(birthday).username(username).email(email)
                .build();

        return repository.save(entity);
    }
}
