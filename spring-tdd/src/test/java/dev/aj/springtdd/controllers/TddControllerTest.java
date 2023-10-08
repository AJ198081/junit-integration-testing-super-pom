package dev.aj.springtdd.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.aj.springtdd.domain.model.Post;
import java.io.File;
import java.nio.file.Files;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = {TddController.class}) //Slice test, @SpringBootTest loads the whole context
@AutoConfigureMockMvc //Enables mockMvc for 'rest' request
class TddControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("classpath:data/posts.json")
    private File jsonFile;

    @BeforeEach
    void setUp() {



    }


    @AfterEach
    void tearDown() {
    }

    @Test
    void shouldFindAllPosts() throws Exception {


        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/posts"))
                                     .andExpect(MockMvcResultMatchers.status()
                                                                     .is2xxSuccessful())
                                     .andExpect(MockMvcResultMatchers.content()
                                                                     .contentType(MediaType.APPLICATION_JSON))
                                     .andExpect(MockMvcResultMatchers.content()
                                                                     .json(Files.readString(jsonFile.toPath())))
                                     .andReturn();

        String responseBody = mvcResult.getResponse()
                                       .getContentAsString();

        List<Post> postList = objectMapper.readValue(responseBody, new TypeReference<>() {
        });

        Assertions.assertEquals(100, postList.size());
    }


}