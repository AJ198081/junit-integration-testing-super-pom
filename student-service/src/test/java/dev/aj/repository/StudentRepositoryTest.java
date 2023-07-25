package dev.aj.repository;

import dev.aj.domain.model.Student;
import java.util.Arrays;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Tag("integration")
class StudentRepositoryTest {

    public static final String EMAIL = "abg@gmail.com";
    private Student studentAJ;
    private Student studentDJ;
    private Student inactiveStudent;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    void setUp() {
        studentAJ = Student.builder()
                           .firstName("AJ")
                           .lastName("B")
                           .email(EMAIL)
                           .active(true)
                           .grade(80.0)
                           .build();

        studentDJ = Student.builder()
                           .firstName("DJ")
                           .lastName("B")
                           .email("djb@gmail.com")
                           .active(true)
                           .grade(90.0)
                           .build();

        inactiveStudent = Student.builder()
                                 .firstName("ZJ")
                                 .lastName("B")
                                 .email("zjb@gmail.com")
                                 .active(false)
                                 .grade(50)
                                 .build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testGetStudentByNameReturnsStudent() {

        // save operation doesn't flush the entity to the database, and we just interact with first level cache when fetching
        Student savedStudent = studentRepository.save(studentAJ);

        Optional<Student> studentFetched = studentRepository.getStudentsByEmail(EMAIL);
        Student fetchedStudent = studentFetched.get();

        Assertions.assertThat(fetchedStudent)
                  .isNotNull()
                  .satisfies(stud -> stud.getFirstName()
                                         .equals("AJ"))
                  .matches(s -> s.getLastName()
                                 .equals("B"));

        Assertions.assertThat(fetchedStudent.getId())
                  .isGreaterThanOrEqualTo(1);

    }

    @Test
    void testGetStudentByNameReturnsStudentFetchedFromDB() {

        // synchronises the persistence context with the underlying database, and then fetch the entity using identifier
        Student savedStudent = testEntityManager.persistFlushFind(studentAJ);

        Optional<Student> studentFetched = studentRepository.getStudentsByEmail(EMAIL);
        Student fetchedStudent = studentFetched.get();

        Assertions.assertThat(fetchedStudent)
                  .isNotNull()
                  .satisfies(stud -> stud.getFirstName()
                                         .equals("AJ"))
                  .matches(s -> s.getLastName()
                                 .equals("B"));

        Assertions.assertThat(fetchedStudent.getId())
                  .isGreaterThanOrEqualTo(1);

    }

    @Test
    @Sql(statements = {
            "insert into student(active, email, first_name, grade, last_name) values (true, 'na@gmail.com', 'na', 100.0, 'b')"
    })
    void testGetAverageGradesForActiveStudents() {

        Arrays.asList(studentAJ, studentDJ, inactiveStudent).forEach(testEntityManager::persistAndFlush);

        double averageGrades = studentRepository.findAverageGradesForActiveStudents();

        Assertions.assertThat(averageGrades)
                  .isEqualTo(90.0);
    }
}
