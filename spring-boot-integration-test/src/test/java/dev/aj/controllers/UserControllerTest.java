package dev.aj.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.aj.domain.dtos.UserDetailsDto;
import dev.aj.domain.dtos.UserDto;
import dev.aj.domain.dtos.UserRestDto;
import dev.aj.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.UUID;

@WebMvcTest(controllers = {UserController.class})
@AutoConfigureMockMvc(addFilters = false) // What do you reckon will happen if we comment this?
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserControllerTest {

    public static final String FIRST_NAME = "Ajs";
    public static final String LAST_NAME = "bsc";
    public static final String EMAIL = "abg@gmail.com";
    public static final String PASSWORD = "password@1";

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    // You can make it a parameterizedTest to test the 'validations' on Dto/Entity class
    @Test
    void Test_Create_User_When_Valid_User_Details_Provided() throws Exception {

        String randomUUID = UUID.randomUUID().toString();

        Mockito.when(userService.createUser(Mockito.any(UserDto.class))).then(invocation -> {
            UserDto userDtoToBeSaved = invocation.getArgument(0, UserDto.class);
            userDtoToBeSaved.setUserId(randomUUID);
            return userDtoToBeSaved;
        });

        UserDetailsDto userDetailsDto = UserDetailsDto.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .password(PASSWORD)
                .repeatPassword(PASSWORD)
                .build();

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDetailsDto)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseJson = mvcResult.getResponse().getContentAsString();
        UserRestDto userRestDto = objectMapper.readValue(responseJson, UserRestDto.class);

        Assertions.assertThat(userRestDto)
                .extracting("firstName", "lastName", "email", "userId")
                .contains(FIRST_NAME, LAST_NAME, EMAIL, randomUUID);
    }

     @Test
    void Test_Create_User_Returns_400_When_In_Valid_User_Details_Provided() throws Exception {

        String randomUUID = UUID.randomUUID().toString();

        Mockito.when(userService.createUser(Mockito.any(UserDto.class))).then(invocation -> {
            UserDto userDtoToBeSaved = invocation.getArgument(0, UserDto.class);
            userDtoToBeSaved.setUserId(randomUUID);
            return userDtoToBeSaved;
        });

        UserDetailsDto userDetailsDto = UserDetailsDto.builder()
                .firstName("A")
                .lastName(LAST_NAME)
                .email(EMAIL)
                .password(PASSWORD)
                .repeatPassword(PASSWORD)
                .build();

         MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                         .contentType(MediaType.APPLICATION_JSON)
                         .accept(MediaType.APPLICATION_JSON)
                         .content(objectMapper.writeValueAsString(userDetailsDto)))
                 .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                 .andReturn();

         Assertions.assertThat(mvcResult)
                 .extracting(MvcResult::getResolvedException)
                 .extracting(Object::getClass)
                 .isEqualTo(MethodArgumentNotValidException.class);
     }
}