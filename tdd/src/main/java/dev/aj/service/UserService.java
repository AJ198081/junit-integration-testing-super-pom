package dev.aj.service;

import dev.aj.domain.model.User;

public interface UserService {

    void createUser();

    User createUser(String firstName, String lastName, String email, String password, String repeatedPassword);

}
