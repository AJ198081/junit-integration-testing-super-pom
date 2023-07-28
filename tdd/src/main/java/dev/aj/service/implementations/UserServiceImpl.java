package dev.aj.service.implementations;

import dev.aj.domain.model.User;
import dev.aj.service.UserService;

public class UserServiceImpl implements UserService {

    @Override
    public void createUser() {

    }

    @Override
    public User createUser(String firstName, String lastName, String email, String password, String repeatedPassword) {

        if (!password.equals(repeatedPassword)) {
            throw new IllegalArgumentException(String.format("Password - %s shall match the Repeated Password - %s", password, repeatedPassword));
        }

        return User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .build();
    }
}
