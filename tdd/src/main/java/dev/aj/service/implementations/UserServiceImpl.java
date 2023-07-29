package dev.aj.service.implementations;

import dev.aj.domain.model.User;
import dev.aj.repository.UserRepository;
import dev.aj.repository.implementation.UserRepositoryImpl;
import dev.aj.service.UserService;

import java.util.UUID;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl() {
        this.userRepository = new UserRepositoryImpl();
    }

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(String firstName, String lastName, String email, String password, String repeatedPassword) {

        if (!password.equals(repeatedPassword)) {
            throw new IllegalArgumentException(String.format("Password - %s shall match the Repeated Password - %s", password, repeatedPassword));
        }

        User newUser = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .build();

        User savedUser = userRepository.save(newUser);

        return savedUser;
    }

    @Override
    public User findUserById(UUID userId) {
        return userRepository.findById(userId);
    }
}
