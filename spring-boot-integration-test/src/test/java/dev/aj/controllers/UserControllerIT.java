package dev.aj.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.aj.domain.dtos.UserLoginDto;
import dev.aj.domain.dtos.UserRestDto;
import dev.aj.security.SecurityConstants;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"server.port=8383"})
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
//@TestPropertySource(locations = {"/application-test.properties"}, properties = {"server.port=8484"})
@TestPropertySource(locations = {"/application-test.properties"})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class UserControllerIT {
    public static final String PASSWORD = "password890";
    public static final String EMAIL = "ajb@sc.com";

/*    @Value("${server.port}")
    private int serverPort;*/

    @LocalServerPort
    private int localServerPort;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;
    private List<String> authorizationHeader;
    private List<String> userIdHeader;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Order(1)
    @Test
    void Create_User_When_Valid_User_Details_Provided() throws JSONException {

        JSONObject userDetailsDto = new JSONObject();
        userDetailsDto.put("firstName", "AJB");
        userDetailsDto.put("lastName", "SCW");
        userDetailsDto.put("email", EMAIL);
        userDetailsDto.put("password", PASSWORD);
        userDetailsDto.put("repeatPassword", PASSWORD);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML));

        HttpEntity<String> httpEntity = new HttpEntity<>(userDetailsDto.toString(), headers);

        ResponseEntity<UserRestDto> userRestDtoResponseEntity = restTemplate.postForEntity("http://localhost:" + localServerPort + "/users", httpEntity, UserRestDto.class);

        org.assertj.core.api.Assertions.assertThat(userRestDtoResponseEntity)
                .extracting(ResponseEntity::getStatusCode, responseEntity -> responseEntity.getBody().getEmail())
                .contains(HttpStatus.OK, EMAIL);
    }

    @Order(2)
    @DisplayName(value = "GET /users requires JWT")
    @Test
    void Test_Get_Users_With_Missing_JWT_Returns_403() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML));

        HttpEntity<HttpHeaders> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<List<UserRestDto>> responseEntity = restTemplate.exchange(
                "/users",
                HttpMethod.GET,
                httpEntity,
                new ParameterizedTypeReference<List<UserRestDto>>() {
                }
        );

        Assertions.assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode(), () -> "Http status Forbidden should have been returned");
    }

    @Order(3)
    @Test
    void testUserLoginWhenValidCredentialsProvidedReturnsJWTAuthorizationHeader() throws JsonProcessingException {
        String jsonPayload = objectMapper.writeValueAsString(UserLoginDto.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .build());

        HttpEntity<String> request = new HttpEntity<>(jsonPayload);

        ResponseEntity<Object> responseEntity = restTemplate.postForEntity("/users/login", request, null);

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), () -> "Http status code of 200 should be returned.");

        authorizationHeader = responseEntity.getHeaders().get(SecurityConstants.HEADER_STRING);
        userIdHeader = responseEntity.getHeaders().get(SecurityConstants.USER_ID);

        Assertions.assertNotNull(authorizationHeader.get(0), () -> "Response headers should contain Authorization");

        org.assertj.core.api.Assertions.assertThat(authorizationHeader.get(0))
                .startsWith("Bearer ");

        Assertions.assertNotNull(userIdHeader.get(0));
    }

    @Test
    @Order(4)
    @DisplayName("GET /users works")
    void testGetUsersWhenValidJWTProvided() {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
        httpHeaders.setBearerAuth(authorizationHeader.get(0).replace(SecurityConstants.TOKEN_PREFIX, ""));

        HttpEntity<Object> httpEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<List<UserRestDto>> responseEntity = restTemplate.exchange("/users",
                HttpMethod.GET,
                httpEntity,
                new ParameterizedTypeReference<List<UserRestDto>>() {}
        );

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), () -> "Should have returned status code of 200");

        org.assertj.core.api.Assertions.assertThat(responseEntity.getBody())
                .size()
                .isPositive();
    }
}