package dev.aj.service;

import dev.aj.domain.model.Student;
import dev.aj.repository.StudentRepository;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

//Ensures that none of the 'web-environment' related beans are brought into context
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StudentServiceTest {

    @Autowired
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

    @Test
    void Get_Student_By_Id_For_Saved_Student() {

        Student savedStudent = studentService.save(student);

        Student studentFetchedFromRepository = studentService.getStudentById(savedStudent.getId());

        Assertions.assertThat(studentFetchedFromRepository)
                  .isNotNull()
                  .isInstanceOf(Student.class)
                  .isIn(List.of(savedStudent));

    }

}