package dev.aj.app.service;

import dev.aj.app.model.CollegeStudent;
import dev.aj.app.repository.CollegeStudentRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@TestPropertySource(locations = "/application.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class StudentServiceTest {

    public static final String EMAIL = "abg@hotmail.com";

    private static CollegeStudent collegeStudent;

    @Autowired // Notice just autowired the service, and didn't mock anything,
    private StudentService studentService;

    @Autowired
    private CollegeStudentRepository studentDao;

    @Autowired // Just a helper class by spring framework to allow execution of jdbc queries on the database
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CollegeStudentRepository collegeStudentRepository;

    @BeforeAll
    static void beforeAll() {
        collegeStudent = CollegeStudent.builder()
                .firstName("AJ")
                .lastName("B")
                .email(EMAIL)
                .build();
    }

    @AfterAll
    static void afterAll() {

    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
//        Can be used to remove any mutual dependency on different tests
//        jdbcTemplate.execute("delete from student");
    }

    @Test
    @Order(1) // Need to save the student first,
    public void TEST_STUDENT_CREATED_SUCCESSFULLY() {

        CollegeStudent savedStudent = studentService.createStudent(collegeStudent);

        //Advantage of assertAll, compared to individual assertions, Runs all tests regardless of failure
        Assertions.assertAll("Test student objects saved to DB is same as was saved",
                () -> Assertions.assertNotNull(savedStudent.getId(), () -> "Expected id of student saved to DB to be not null, but was"),
                () -> Assertions.assertEquals(savedStudent.getFirstName(), collegeStudent.getFirstName(), () -> "Expected first name of student saved to the DB to be same as the one that was provided, but were different"),
                () -> Assertions.assertEquals(savedStudent.getLastName(), collegeStudent.getLastName(), () -> "Expected Last name of student saved to the DB to be same as the one that was provided, but were different"),
                () -> Assertions.assertEquals(savedStudent.getEmail(), collegeStudent.getEmail(), () -> "Expected Email of student saved to the DB to be same as the one that was provided, but were different")
        );
    }

    @Test
    @Order(2) // Before it can be fetched
    public void TEST_SAVED_STUDENT_CAN_BE_FETCHED_SUCCESSFULLY() {
        CollegeStudent studentFromDB = studentService.findStudentByEmail(EMAIL);
        Assertions.assertAll("Test student object fetched from DB is equal to the one that was saved",
                () -> Assertions.assertNotNull(studentFromDB.getId(), () -> "Expected id of student to not be null, but was"),
                () -> Assertions.assertEquals(collegeStudent.getFirstName(), studentFromDB.getFirstName(), () -> "Expected first name of student to be same, but were different"),
                () -> Assertions.assertEquals(collegeStudent.getLastName(), studentFromDB.getLastName(), () -> "Expected Last name of student to be same, but were different"),
                () -> Assertions.assertEquals(collegeStudent.getEmail(), studentFromDB.getEmail(), () -> "Expected Email of student to be same, but were different")
        );
    }

    @Test
    @Order(3)
    public void TEST_SAVE_OPERATION_THROUGH_JDBC_TEMPLATE() {
        jdbcTemplate.execute("insert into student(first_name, last_name, email) values ('DJ', 'B', 'djb@hotmail.com')");

        List<CollegeStudent> collegeStudents = jdbcTemplate.query("select id, first_name, last_name, email from student", (rs, rowNum) -> {
            return CollegeStudent.builder()
                    .id(rs.getInt(1))
                    .firstName(rs.getString(2))
                    .lastName(rs.getString(3))
                    .email(rs.getString(4))
                    .build();
        });
        org.assertj.core.api.Assertions.assertThat(collegeStudents.size()).isGreaterThanOrEqualTo(1);
    }

    @Test
    public void TEST_STUDENT_CAN_BE_DELETED_SUCCESSFULLY() {

        jdbcTemplate.execute("insert into student(first_name, last_name, email) values ('DJ', 'B', 'djb@hotmail.com')");

        studentDao.deleteById(3);

        Optional<CollegeStudent> deletedStudent = studentDao.findById(3);

        Assertions.assertFalse(deletedStudent.isPresent(), () -> "Deleted student shouldn't have been present, but was");
    }

    @Sql(scripts = "/insertData.sql")
//Executes the provided SQL just prior to execution of this method, but after @BeforeEach, just required a datasource configured it will work
    @Test
    @Order(-1)
    // These @Sql are direct DB insertion statements, executed on the DB directly, so ensure carried out upfront, other may have primary key already exist exception
    public void GET_GRADE_BOOK_SERVICE() {

        studentService.createStudent(collegeStudent);

        List<CollegeStudent> allStudents = studentService.getGradebook();
        org.assertj.core.api.Assertions.assertThat(allStudents.size()).isGreaterThanOrEqualTo(5);
    }

    @Sql(statements = {"insert into student(first_name, last_name, email) values ('ZJ', 'B', 'zjb@gmail.com')",
            "insert into student(first_name, last_name, email) values ('DJ', 'B', 'djb@gmail.com')",
            "insert into student(first_name, last_name, email) values ('R', 'B', 'rb@gmail.com')",
            "insert into student(first_name, last_name, email) values ('D', 'S', 'ds@gmail.com')"
    }) //Can provide an array of Sql instead as well, so long there is a datasource configured it will work
    @Test
    @Order(-2)
    public void TEST_SQL_ANNOTATION_WORKING() {

        studentService.createStudent(collegeStudent);

        List<CollegeStudent> allStudents = studentService.getGradebook();
        org.assertj.core.api.Assertions.assertThat(allStudents.size()).isGreaterThanOrEqualTo(5);
    }
}