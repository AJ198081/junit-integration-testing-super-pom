package dev.aj.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.aj.domain.model.Student;
import dev.aj.service.StudentService;
import java.util.List;
import java.util.NoSuchElementException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class StudentControllerIT {

    public static final String EMAIL = "abg@hotmail.com";
    private static final String FIRST_NAME = "AJ";
    private static final String LAST_NAME = "B";
    private static final double GRADES = 80.0;
    private Student student;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        student = Student.builder()
                         .firstName(FIRST_NAME)
                         .lastName(LAST_NAME)
                         .grade(GRADES)
                         .email(EMAIL)
                         .build();
    }

    @Sql(statements = "insert into student(active, email, first_name, grade, last_name) values (true, 'na@gmail.com', 'na', 100.0, 'b')")
    @Test
    void updateStudent() throws Exception {

        List<Student> students = studentService.getAllStudents();

        Long studentId = students.stream()
                                 .map(Student::getId)
                                 .findFirst()
                                 .orElseThrow(() -> new NoSuchElementException("No student found!!"));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/students/{id}", studentId)
                                                                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                                    .content(objectMapper.writeValueAsString(student)))
                                     .andExpect(MockMvcResultMatchers.status()
                                                                     .is2xxSuccessful())
                                     .andExpectAll(jsonPath("$.id").value(Matchers.is(1)),
                                             jsonPath("$.email").value(EMAIL),
                                             jsonPath("$.grade").value(100.0))
                                     .andReturn();

        System.out.printf("Student email %s has been changed to %s", "na@gmail.com",
                objectMapper.readValue(mvcResult.getResponse()
                                                .getContentAsString(), Student.class)
                            .getEmail());
    }
}