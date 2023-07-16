package dev.aj;

import dev.aj.model.CollegeStudent;
import dev.aj.model.Student;
import dev.aj.model.StudentGrades;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;


@SpringBootTest
//Try using the same package structure, otherwise add 'classes' to point to your springboot bootstrapping application
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ApplicationTest {

    public static final List<Double> DOUBLE_LIST = List.of(23.0, 88.9, 99.9);

    @Autowired
    ApplicationContext applicationContext;

    @Value("${server.port}")
    private String port;

    @Autowired
    private CollegeStudent collegeStudent;

    @Autowired
    private StudentGrades studentGrades;

    @BeforeEach
    void setUp() {
        studentGrades.setMathGradeResult(DOUBLE_LIST);

        collegeStudent.setFirstName("AJ");
        collegeStudent.setLastName("B");
        collegeStudent.setEmail("abg@sc.com");
        collegeStudent.setStudentGrades(studentGrades);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Order(1)
    void TEST_CONTEXT_LOADS() {
        Student collegeStudent = applicationContext.getBean("collegeStudent", Student.class);
        Assertions.assertEquals(CollegeStudent.class, collegeStudent.getClass(), () -> "Should return a bean of 'CollegeStudent.class'");
    }

    @Test
    @Order(2)
    void TEST_PROPERTY_SOURCE_ACCESSIBLE() {
        Assertions.assertEquals("8081", port, () -> "Should have returned '8080', from application.properties");
    }

    @Test
    @Order(3)
    void TEST_STUDENT_GRADE_RESULTS_SET_CORRECTLY() {
        Assertions.assertEquals(DOUBLE_LIST, collegeStudent.getStudentGrades().getMathGradeResult());
    }

    @Test
    @Order(4)
    void TEST_COLLEGE_STUDENT_BEAN_TYPE_IS_PROTOTYPE() {
        CollegeStudent collegeStudentBean = applicationContext.getBean(CollegeStudent.class);
        Assertions.assertNotEquals(collegeStudent, collegeStudentBean);
    }

    @Test
    @Order(5)
    void TEST_BEAN_SCOPE_SINGLETON_WHEN_NO_SCOPE_DEFINED() {
        StudentGrades studentGradesBean = applicationContext.getBean(StudentGrades.class);

        Assertions.assertAll("Check Singleton Scope with two different methods",
                () -> Assertions.assertEquals(studentGradesBean, studentGrades),
                () -> Assertions.assertSame(studentGradesBean, studentGrades)
        );
    }
}