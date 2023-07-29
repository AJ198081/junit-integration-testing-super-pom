package dev.aj.service;

import dev.aj.domain.model.User;
import dev.aj.repository.UserRepository;
import dev.aj.service.implementations.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;
import java.util.stream.Stream;

@DisplayName(value = "UserService Tests")
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

//    private UserService userService;

    private static Stream<Arguments> validUserArgumentsProvider() {
        return Stream.of(
                Arguments.of("AJ", "B", "abg@gmail.com", "password1", "password1"),
                Arguments.of("DJ", "B", "djb@gmail.com", "password2", "password2")
        );
    }

    private static Stream<Arguments> mismatchPasswordsUserArgumentProvider() {
        return Stream.of(
                Arguments.of("AJ", "B", "abg@gmail.com", "password1", "password2"),
                Arguments.of("DJ", "B", "djb@gmail.com", "password2", "password1")
        );
    }

    @BeforeEach
    void setUp() {
//        userService = new UserServiceImpl();
    }

    @AfterEach
    void tearDown() {
        userService = null;
    }

    @ParameterizedTest
    @MethodSource(value = "validUserArgumentsProvider")
    void Test_Create_User_When_Correct_Details_Provided(String firstName, String lastName, String email, String password, String repeatedPassword) {

        User expectedUser = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .build();

        Mockito.when(userRepository.save(ArgumentMatchers.any(User.class))).then(invocation -> {
            User userToBeSaved = invocation.getArgument(0, User.class);
            userToBeSaved.setId(UUID.randomUUID());
            return userToBeSaved;
        });

        Mockito.when(userRepository.findById(ArgumentMatchers.any(UUID.class))).then(invocation -> {
            UUID userId = invocation.getArgument(0, UUID.class);
            expectedUser.setId(userId);
            return expectedUser;
        });

        User createdUser = userService.createUser(firstName, lastName, email, password, repeatedPassword);

        UUID createdUserId = createdUser.getId();

        User fetchedUser = userService.findUserById(createdUserId);

        expectedUser.setId(createdUserId);

        org.assertj.core.api.Assertions.assertThat(createdUser).usingRecursiveComparison().isEqualTo(expectedUser);

        org.assertj.core.api.Assertions.assertThat(fetchedUser).usingRecursiveComparison().isEqualTo(createdUser);

        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
    }

    @ParameterizedTest
    @MethodSource(value = "mismatchPasswordsUserArgumentProvider")
//    @Disabled
    void Test_Create_User_Throws_Error_When_Passwords_Not_Match(String firstName, String lastName, String email, String password, String repeatedPassword) {

        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.createUser(firstName, lastName, email, password, repeatedPassword));

        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(User.class));

    }

}
