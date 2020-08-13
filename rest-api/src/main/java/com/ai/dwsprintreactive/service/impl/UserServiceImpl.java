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
    public Mono<User> update(Integer id, User differentUser) {
        return get(id)
                .map(existingUser -> {
                    Integer userId = existingUser.getId();
                    UUID uuid = existingUser.getUuid();
                    String firstName = differentUser.getFirstName() != null ? differentUser.getFirstName() : existingUser.getFirstName();
                    String lastName = differentUser.getLastName() != null ? differentUser.getLastName() : existingUser.getLastName();
                    Double weight = differentUser.getWeight() != null ? differentUser.getWeight() : existingUser.getWeight();
                    Double height = differentUser.getHeight() != null ? differentUser.getHeight() : existingUser.getHeight();
                    String gender = differentUser.getGender() != null ? differentUser.getGender() : existingUser.getGender();
                    LocalDate birthday = differentUser.getBirthday() != null ? differentUser.getBirthday() : existingUser.getBirthday();
                    String username = differentUser.getUsername() != null ? differentUser.getUsername() : existingUser.getUsername();
                    String email = differentUser.getEmail() != null ? differentUser.getEmail() : existingUser.getEmail();
                    LocalDateTime createdAt = differentUser.getCreatedAt() != null ? differentUser.getCreatedAt() : existingUser.getCreatedAt();

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
                .flatMap(user -> repository.deleteById(user.getId()));
    }

    @Override
    public Mono<User> create(UUID uuid, String firstName, String lastName, Double weight, Double height,
                             String gender, LocalDate birthday, String username, String email) {

        User user = User.builder()
                .uuid(uuid).firstName(firstName).lastName(lastName)
                .weight(weight).height(height).gender(gender)
                .birthday(birthday).username(username).email(email)
                .build();

        return repository.save(user);
    }
}
