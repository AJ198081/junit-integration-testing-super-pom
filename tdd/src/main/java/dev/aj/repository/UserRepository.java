package dev.aj.repository;

import dev.aj.domain.model.User;

import java.util.UUID;

public interface UserRepository {

    User save(User user);

    User findById(UUID id);

}
