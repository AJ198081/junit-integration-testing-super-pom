package dev.aj.controller;

import dev.aj.domain.model.Student;
import dev.aj.service.StudentService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

//Doesn't bring into context any 'Service', 'Repository' or 'Component', whatever is necessary for 'Controller', Jackson - yes.
@WebMvcTest
class StudentControllerTest {

    private static final String FIRST_NAME = "AJ";
    private static final String LAST_NAME = "B";
    private static final double GRADES = 80.0;

    private Student student;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        student = Student.builder()
                         .firstName(FIRST_NAME)
                         .lastName(LAST_NAME)
                         .grade(GRADES)
                         .build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getStudentForSavedStudent() throws Exception {

        student.setId(1l);

        Mockito.when(studentService.getStudentById(1l))
               .thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders.get("/students/1"))
               .andExpect(MockMvcResultMatchers.status()
                                               .is2xxSuccessful())
               .andExpectAll(jsonPath("$.id").value(Matchers.is(1)),
                       jsonPath("$.firstName").value(FIRST_NAME),
                       jsonPath("$.lastName").value(LAST_NAME),
                       jsonPath("$.grade").value(80));

    }
}