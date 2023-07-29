package dev.aj.service;

import dev.aj.domain.model.User;

import java.util.UUID;

public interface UserService {

    User createUser(String firstName, String lastName, String email, String password, String repeatedPassword);

    User findUserById(UUID userId);
}
