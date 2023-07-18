package dev.aj.app.service;

import dev.aj.app.model.CollegeStudent;
import dev.aj.app.model.Gradebook;
import dev.aj.app.model.HistoryGrade;
import dev.aj.app.model.enums.GradeType;
import dev.aj.app.repository.CollegeStudentRepository;
import dev.aj.app.repository.HistoryGradeRepository;
import dev.aj.app.repository.MathGradeRepository;
import dev.aj.app.repository.ScienceGradeRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.hamcrest.Matchers;
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
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
    public static final int STUDENT_ID = 1;

    private static CollegeStudent collegeStudent;

    @Autowired // Notice just autowired the service, and didn't mock anything,
    private StudentService studentService;

    @Autowired
    private CollegeStudentRepository studentDao;

    @Autowired // Just a helper class by spring framework to allow execution of jdbc queries on the database
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CollegeStudentRepository collegeStudentRepository;

    @Autowired
    private MathGradeRepository mathGradeRepository;
    @Autowired
    private HistoryGradeRepository historyGradeRepository;
    @Autowired
    private ScienceGradeRepository scienceGradeRepository;

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
        jdbcTemplate.execute("delete from student");
        jdbcTemplate.execute("delete from math_grade");
        jdbcTemplate.execute("delete from history_grade");
        jdbcTemplate.execute("delete from science_grade");

    }

    @Test
    @Order(STUDENT_ID) // Need to save the student first,
    public void TEST_STUDENT_CREATED_SUCCESSFULLY() {

        CollegeStudent savedStudent = studentService.createStudent(collegeStudent);

        //Advantage of assertAll, compared to individual assertions, Runs all tests regardless of failure
        Assertions.assertAll("Test student objects saved to DB is same as was saved",
                () -> org.hamcrest.MatcherAssert.assertThat(savedStudent.getId(), Matchers.greaterThan(0L)),
                () -> Assertions.assertNotNull(savedStudent.getId(), () -> "Expected id of student saved to DB to be not null, but was"),
                () -> Assertions.assertEquals(savedStudent.getFirstName(), collegeStudent.getFirstName(), () -> "Expected first name of student saved to the DB to be same as the one that was provided, but were different"),
                () -> Assertions.assertEquals(savedStudent.getLastName(), collegeStudent.getLastName(), () -> "Expected Last name of student saved to the DB to be same as the one that was provided, but were different"),
                () -> Assertions.assertEquals(savedStudent.getEmail(), collegeStudent.getEmail(), () -> "Expected Email of student saved to the DB to be same as the one that was provided, but were different")
        );
    }

    @Test
    @Order(2) // Before it can be fetched
    public void TEST_SAVED_STUDENT_CAN_BE_FETCHED_SUCCESSFULLY() {
        studentService.createStudent(collegeStudent);
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
                    .id(rs.getLong(STUDENT_ID))
                    .firstName(rs.getString(2))
                    .lastName(rs.getString(3))
                    .email(rs.getString(4))
                    .build();
        });
        org.assertj.core.api.Assertions.assertThat(collegeStudents.size()).isGreaterThanOrEqualTo(STUDENT_ID);
    }

    @Test
    public void TEST_STUDENT_CAN_BE_DELETED_SUCCESSFULLY() {

        jdbcTemplate.execute("insert into student(first_name, last_name, email) values ('DJ', 'B', 'djb@hotmail.com')");

        studentDao.deleteById(3L);

        Optional<CollegeStudent> deletedStudent = studentDao.findById(3L);

        Assertions.assertFalse(deletedStudent.isPresent(), () -> "Deleted student shouldn't have been present, but was");
    }

    @Sql(scripts = "/insertData.sql")
    //Executes the provided SQL just prior to execution of this method, but after @BeforeEach, just required a datasource configured it will work
    @Test
    @Order(-STUDENT_ID)
    // These @Sql are direct DB insertion statements, executed on the DB directly, carried out upfront, beware of primary key already exist exceptions
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

    @Test
    @Order(10)
    void Test_Add_Math_Grade() {

        CollegeStudent newStudent = studentService.createStudent(collegeStudent);

        Assertions.assertTrue(studentService.createGrade(80.50, newStudent.getId(), GradeType.MATH));

        org.hamcrest.MatcherAssert.assertThat(mathGradeRepository.count(), Matchers.greaterThanOrEqualTo(1l));
    }

    @Test
    @Order(11)
    void Test_Add_History_Grade() {

        CollegeStudent student = studentService.createStudent(collegeStudent);

        Assertions.assertTrue(studentService.createGrade(80.00, student.getId(), GradeType.HISTORY));

        org.hamcrest.MatcherAssert.assertThat(historyGradeRepository.count(), Matchers.greaterThanOrEqualTo(1l));
    }

    @Test
    @Order(12)
    void Test_Add_Science_Grade() {

        CollegeStudent student = studentService.createStudent(collegeStudent);

        Assertions.assertTrue(studentService.createGrade(80.00, student.getId(), GradeType.SCIENCE));

        org.hamcrest.MatcherAssert.assertThat(scienceGradeRepository.count(), Matchers.greaterThanOrEqualTo(1l));
    }

    @ParameterizedTest(name = "Invocation # {index} with grade value of {arguments}")
    @ValueSource(doubles = {
            -5, 105, 100.01, -0.01
    })
    void Test_Grades_Outside_Zero_And_Hundred_Returns_False(double grade) {
        Assertions.assertFalse(studentService.createGrade(grade, studentService.createStudent(collegeStudent).getId(), GradeType.MATH));
    }

    @ParameterizedTest(name = "Invocation #{index} with grade value of {0} and type {1}")
    @CsvSource({
            "100.01, math",
            "-0.01, math",
            "100.01, science",
            "-0.01, science",
            "100.01, history",
            "-0.01, history",
            "100.01, finance",
            "-0.01, finance"
    })
    void Test_Grades_Outside_Zero_And_Hundred_Returns_False(double grade, String gradeType) {
        String gradeTypeForEnum = gradeType.toUpperCase();

        //Technically, should have tested in different methods for scenario when enum doesn't exist, but not the purpose here.
        try {
            GradeType gradeTypeForEntry = GradeType.valueOf(gradeTypeForEnum);
            Assertions.assertFalse(studentService.createGrade(grade, studentService.createStudent(collegeStudent).getId(), gradeTypeForEntry));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    @Sql(statements = {
            "insert into student(id, first_name, last_name, email) values(1, 'AJ', 'B', 'abg@hotmail.com')",
            "insert into math_grade(student_id, grade) values(1, 80.0)",
            "insert into math_grade(student_id, grade) values(1, 85.0)"
    })
    @Test
    void Test_Delete_Math_Grade() {

        List<Map<Long, Long>> listOfGradeAndStudentMap = jdbcTemplate.query("select id, student_id from math_grade", (rs, rowNum) -> {
            return Map.of(rs.getLong(1), rs.getLong(2));
        });

        listOfGradeAndStudentMap.forEach(longLongMap -> longLongMap.forEach((key, value) -> {
            Long actualStudentId = studentService.deleteGrade(key, GradeType.MATH);
            Assertions.assertEquals(value, actualStudentId,
                    () -> String.format("Expected studentId %s, but received %s", value, actualStudentId));
        }));
    }

    @Sql(statements = {
            "insert into student(id, first_name, last_name, email) values(2, 'AJ', 'B', 'abg@hotmail.com')",
            "insert into science_grade(student_id, grade) values(2, 85.0)"
    })
    @Test
    void Test_Delete_Science_Grade() {

        List<Map<Long, Long>> listOfGradeAndStudentMap = jdbcTemplate.query("select id, student_id from science_grade", (rs, rowNum) -> {
            return Map.of(rs.getLong(1), rs.getLong(2));
        });

        listOfGradeAndStudentMap.forEach(longLongMap -> longLongMap.forEach((key, value) -> {
            Long actualStudentId = studentService.deleteGrade(key, GradeType.SCIENCE);
            Assertions.assertEquals(value, actualStudentId,
                    () -> String.format("Expected studentId %s, but received %s", value, actualStudentId));
        }));
    }

    @Sql(statements = {
            "insert into student(id, first_name, last_name, email) values(3, 'AJ', 'B', 'abg@hotmail.com')",
            "insert into history_grade(student_id, grade) values(3, 80.0)"
    })
    @Test
    void Test_Delete_History_Grade() {

        List<Map<Long, Long>> listOfGradeAndStudentMap = jdbcTemplate.query("select id, student_id from history_grade", (rs, rowNum) -> {
            return Map.of(rs.getLong(1), rs.getLong(2));
        });

        listOfGradeAndStudentMap.forEach(longLongMap -> longLongMap.forEach((key, value) -> {
            Long actualStudentId = studentService.deleteGrade(key, GradeType.HISTORY);
            Assertions.assertEquals(value, actualStudentId,
                    () -> String.format("Expected studentId %s, but received %s", value, actualStudentId));
        }));
    }

    @Test
    void Test_Delete_History_Grade_With_Non_Existence_Student_Id() {
        Long studentGradeOfDeletedStudent = studentService.deleteGrade(1L, GradeType.HISTORY);
        Assertions.assertEquals(0L, studentGradeOfDeletedStudent, () -> String.format("Should have returned the Student Id - %s of the deleted student, but returned %s.", STUDENT_ID, studentGradeOfDeletedStudent));
    }

    @Sql(statements = {
            "insert into student(id, first_name, last_name, email) values(3, 'AJ', 'B', 'abg@hotmail.com')",
            "insert into history_grade(student_id, grade) values(3, 80.0)"
    })
    @Test
    void Test_Delete_Grade_By_Non_Existence_Grade_Type() {
        List<Map<Long, Long>> listOfGradeAndStudentMap = jdbcTemplate.query("select id, student_id from science_grade", (rs, rowNum) -> {
            return Map.of(rs.getLong(1), rs.getLong(2));
        });

        listOfGradeAndStudentMap.forEach(longLongMap -> longLongMap.forEach((key, value) -> {
            Long actualStudentId = studentService.deleteGrade(key, GradeType.SCIENCE);
            Assertions.assertEquals(0L, actualStudentId,
                    () -> String.format("Expected no studentId to be deleted, but was %s.", actualStudentId));
        }));
    }

    @Sql(statements = {
            "insert into student(id, first_name, last_name, email) values(1, 'AJ', 'B', 'abg@hotmail.com')",
            "insert into math_grade(student_id, grade) values(1, 80.0)",
            "insert into science_grade(student_id, grade) values(1, 81.0)",
            "insert into history_grade(student_id, grade) values(1, 82.0)",
    })
    @Test
    void Test_Delete_Student_Deletes_Associated_Grades() {

        Optional<CollegeStudent> student = studentDao.findById(1L);
        Optional<HistoryGrade> historyGradeByStudentId = historyGradeRepository.findHistoryGradeByStudentId(1L);

        Assertions.assertAll("Test student and associated grade is present",
                () -> Assertions.assertTrue(student.isPresent(), () -> "Expected student to be present in DB, but wasn't."),
                () -> Assertions.assertTrue(historyGradeByStudentId.isPresent(), () -> "Expected history grade to be present in DB, but wasn't."),
                () -> Assertions.assertTrue(scienceGradeRepository.findScienceGradeByStudentId(1L).isPresent(), () -> "Expected science grade to be present in DB, but wasn't."),
                () -> Assertions.assertTrue(mathGradeRepository.findMathGradeByStudentId(1L).isPresent(), () -> "Expected math grade to be present in DB, but wasn't.")
        );

        studentService.deleteStudentById(1L);

        Assertions.assertAll("Test student and associated grade aren't present",
                () -> Assertions.assertFalse(studentDao.findById(1L).isPresent(), () -> "Expected student to be deleted in DB, but wasn't."),
                () -> Assertions.assertFalse(historyGradeRepository.findHistoryGradeByStudentId(1L).isPresent(), () -> "Expected history grade to be deleted in DB, but wasn't."),
                () -> Assertions.assertFalse(scienceGradeRepository.findScienceGradeByStudentId(1L).isPresent(), () -> "Expected science grade to be deleted in DB, but wasn't."),
                () -> Assertions.assertFalse(mathGradeRepository.findMathGradeByStudentId(1L).isPresent(), () -> "Expected math grade to be deleted in DB, but wasn't.")
        );
    }
    @Sql(statements = {
            "insert into student(id, first_name, last_name, email) values(1, 'AJ', 'B', 'abg@hotmail.com')",
            "insert into student(id, first_name, last_name, email) values(2, 'DJ', 'B', 'djg@hotmail.com')",
            "insert into math_grade(student_id, grade) values(1, 80.0)",
            "insert into math_grade(student_id, grade) values(2, 80.0)",
            "insert into science_grade(student_id, grade) values(1, 81.0)",
            "insert into science_grade(student_id, grade) values(2, 81.0)",
            "insert into history_grade(student_id, grade) values(1, 82.0)",
            "insert into history_grade(student_id, grade) values(2, 82.0)",
    })

    @Test
    void Test_Delete_Student_Does_Not_Delete_Non_Associated_Grades() {

        Optional<CollegeStudent> student = studentDao.findById(1L);
        Optional<HistoryGrade> historyGradeByStudentId = historyGradeRepository.findHistoryGradeByStudentId(1L);

        Assertions.assertAll("Test student and associated grade is present",
                () -> Assertions.assertTrue(student.isPresent(), () -> "Expected student to be present in DB, but wasn't."),
                () -> Assertions.assertTrue(historyGradeByStudentId.isPresent(), () -> "Expected history grade to be present in DB, but wasn't."),
                () -> Assertions.assertTrue(scienceGradeRepository.findScienceGradeByStudentId(1L).isPresent(), () -> "Expected science grade to be present in DB, but wasn't."),
                () -> Assertions.assertTrue(mathGradeRepository.findMathGradeByStudentId(1L).isPresent(), () -> "Expected math grade to be present in DB, but wasn't."),
                () -> Assertions.assertTrue(historyGradeRepository.findHistoryGradeByStudentId(2L).isPresent(), () -> "Expected history grade to be present in DB, but wasn't."),
                () -> Assertions.assertTrue(scienceGradeRepository.findScienceGradeByStudentId(2L).isPresent(), () -> "Expected science grade to be present in DB, but wasn't."),
                () -> Assertions.assertTrue(mathGradeRepository.findMathGradeByStudentId(2L).isPresent(), () -> "Expected math grade to be present in DB, but wasn't.")
        );

        studentService.deleteStudentById(1L);

        Assertions.assertAll("Test student and associated grade aren't present",
                () -> Assertions.assertFalse(studentDao.findById(1L).isPresent(), () -> "Expected student to be deleted in DB, but wasn't."),
                () -> Assertions.assertFalse(historyGradeRepository.findHistoryGradeByStudentId(1L).isPresent(), () -> "Expected history grade to be deleted in DB, but wasn't."),
                () -> Assertions.assertFalse(scienceGradeRepository.findScienceGradeByStudentId(1L).isPresent(), () -> "Expected science grade to be deleted in DB, but wasn't."),
                () -> Assertions.assertFalse(mathGradeRepository.findMathGradeByStudentId(1L).isPresent(), () -> "Expected math grade to be deleted in DB, but wasn't."),
                () -> Assertions.assertTrue(historyGradeRepository.findHistoryGradeByStudentId(2L).isPresent(), () -> "Expected history grade to be present in DB, but wasn't."),
                () -> Assertions.assertTrue(scienceGradeRepository.findScienceGradeByStudentId(2L).isPresent(), () -> "Expected science grade to be present in DB, but wasn't."),
                () -> Assertions.assertTrue(mathGradeRepository.findMathGradeByStudentId(2L).isPresent(), () -> "Expected math grade to be present in DB, but wasn't.")

        );
    }

    @Test
    void Test_Student_Information_Retrieved_Successfully() {
        Assertions.assertIterableEquals(List.of(), studentService.getGradebook(), () -> "Should have returned grade book.");
    }


}