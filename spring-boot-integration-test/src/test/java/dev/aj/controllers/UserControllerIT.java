package dev.aj.controllers;

import dev.aj.domain.dtos.UserDetailsDto;
import dev.aj.domain.dtos.UserRestDto;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"server.port=8383"})
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
//@TestPropertySource(locations = {"/application-test.properties"}, properties = {"server.port=8484"})
@TestPropertySource(locations = {"/application-test.properties"})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserControllerIT {

/*    @Value("${server.port}")
    private int serverPort;*/

    @LocalServerPort
    private int localServerPort;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void Create_User_When_Valid_User_Details_Provided() throws JSONException {

        JSONObject userDetailsDto = new JSONObject();
        userDetailsDto.put("firstName", "AJB");
        userDetailsDto.put("lastName", "SCW");
        userDetailsDto.put("email", "ajb@sc.com");
        userDetailsDto.put("password", "password890");
        userDetailsDto.put("repeatPassword", "password890");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML));

        HttpEntity<String> httpEntity = new HttpEntity<>(userDetailsDto.toString(), headers);

        ResponseEntity<UserRestDto> userRestDtoResponseEntity = restTemplate.postForEntity("http://localhost:" + localServerPort + "/users", httpEntity, UserRestDto.class);

        org.assertj.core.api.Assertions.assertThat(userRestDtoResponseEntity)
                .extracting(ResponseEntity::getStatusCode, responseEntity -> responseEntity.getBody().getEmail())
                .contains(HttpStatus.OK, "ajb@sc.com");
    }

    @Test
    void getUsers() {
    }
}