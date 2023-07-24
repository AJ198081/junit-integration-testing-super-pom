package dev.aj.service;

import dev.aj.domain.model.Student;
import dev.aj.repository.StudentRepository;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StudentServiceCacheTest {

    @MockBean
    private StudentRepository studentRepository;

    @Autowired
    private StudentService studentService;

    private Student student;

    @BeforeEach
    void setUp() {
        student = Student.builder()
                         .firstName("AJ")
                         .lastName("B")
                         .email("abh@gmail.com")
                         .grade(23.0)
                         .build();
    }

    @AfterEach
    void tearDown() {
    }

    @Sql(statements = "")
    @Test
    @Order(2)
    void getStudentById() {

        student.setId(1L);

        Mockito.when(studentRepository.findById(1L))
               .thenReturn(Optional.ofNullable(student));

        Student studentFetchedFromRepository1stTime = studentService.getStudentById(1L);
        Student studentFetchedFromRepository2ndTime = studentService.getStudentById(1L);
        Student studentFetchedFromRepository3rdTime = studentService.getStudentById(1L);
        Student studentFetchedFromRepository4thTime = studentService.getStudentById(1L);

        Mockito.verify(studentRepository, Mockito.times(1))
               .findById(1L);

    }

    @Sql(statements = {
            "insert into student(active, email, first_name, grade, last_name) values (true, 'na@gmail.com', 'na', 100.0, 'b')"
    })
    @Test
    @Order(1)
    void save() {
        Assertions.assertTrue(true);
    }
}