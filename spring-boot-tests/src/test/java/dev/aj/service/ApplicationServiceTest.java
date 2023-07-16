package dev.aj.service;

import dev.aj.Application;
import dev.aj.dao.ApplicationDao;
import dev.aj.model.CollegeStudent;
import dev.aj.model.StudentGrades;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.times;

@SpringBootTest(classes = Application.class)
class ApplicationServiceTest {

    private static final List<Double> DOUBLE_LIST = List.of(23.0, 88.9, 99.9);

    //    @Mock
    @MockBean // Replaces or adds a bean to application context, which can be then autowired
    // Good practice to use when you are injecting Context Beans, and Mocked Beans as dependencies
    private ApplicationDao applicationDao;

    //    @InjectMocks
    @Autowired // Use when you are injecting combination of Mocked and Context beans
    private ApplicationService applicationService;

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
    void addGradeResultsForSingleClass() {

        Mockito.when(applicationDao.addGradeResultsForSingleClass(DOUBLE_LIST)).thenReturn(230.0);

        Assertions.assertEquals(230.0, applicationService.addGradeResultsForSingleClass(DOUBLE_LIST));

        Mockito.verify(applicationDao, times(1)).addGradeResultsForSingleClass(DOUBLE_LIST);

    }

    @Test
    void findGradePointAverage() {
        Mockito.when(applicationDao.findGradePointAverage(DOUBLE_LIST)).thenReturn(43.9);

        Assertions.assertAll("Assert calculation and performance of gradePointAverage",
                () -> Assertions.assertEquals(43.9, applicationService.findGradePointAverage(DOUBLE_LIST)),
                () -> Mockito.verify(applicationDao, times(1)).findGradePointAverage(DOUBLE_LIST)
        );
    }

    @ParameterizedTest(name = "Input = {0}, Value = {1}")
    @CsvSource({
            "newString, newString",
            ",null",
            "null, null"
    })
    void nullSafeObject(Object input, Object output) {
        Mockito.when(applicationDao.checkForNull(input)).thenAnswer(invocation -> {
            Object inputObject = invocation.getArgument(0);
            return Objects.nonNull(inputObject) ? inputObject : "null";
        });

        Assertions.assertEquals(output, applicationService.nullSafeObject(input));
    }

    @Test
    void nullSafeObjectOrThrow() {
        Mockito.when(applicationDao.checkForNull(null)).thenAnswer(invocation -> {
            Object inputObject = invocation.getArgument(0);
//            Object output = Optional.ofNullable(inputObject).orElseThrow(() -> new NullPointerException());
            return Objects.requireNonNull(inputObject);
        });


        Assertions.assertThrows(NullPointerException.class, () -> applicationService.nullSafeObject(null), () -> "Should have thrown NPE, but didn't");
    }
}