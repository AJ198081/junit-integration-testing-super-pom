package dev.aj.service;

import dev.aj.domain.model.User;
import dev.aj.service.UserService;
import dev.aj.service.implementations.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

class UserServiceTest {

    private UserService userService;

    private static Stream<Arguments> userArgumentProvider() {
        return Stream.of(
                Arguments.of("AJ", "B", "abg@gmail.com", "password1", "password1")
        );
    }

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl();
    }

    @AfterEach
    void tearDown() {
        userService = null;
    }

    @ParameterizedTest
    @MethodSource(value = "userArgumentProvider")
    void testCreateUserWhenCorrectDetailsProvided(String firstName, String lastName, String email, String password, String repeatedPassword) {

//        Assertions.assertDoesNotThrow(() -> userService.createUser(firstName, lastName, email, password, repeatedPassword));

        User expectedUser = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .build();

        User createdUser = userService.createUser(firstName, lastName, email, password, repeatedPassword);

        org.assertj.core.api.Assertions.assertThat(createdUser)
                .usingRecursiveComparison()
                .isEqualTo(expectedUser);

    }
}