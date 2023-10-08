package dev.aj.springtdd.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.aj.springtdd.domain.model.Post;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TddController {

    @Value("classpath:data/posts.json")
    private File jsonFile;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping(path = "/posts")
    public ResponseEntity<List<Post>> getAllPosts() throws IOException {
        List<Post> postList = objectMapper.readValue(jsonFile, new TypeReference<>() {
        });

        return new ResponseEntity<>(postList, HttpStatus.OK);
    }
}
