package dev.aj.model;

import dev.aj.dao.ApplicationDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest
class CollegeStudentTest {

    @Autowired
    ApplicationContext applicationContext;

    @MockBean //If using 'SpringBootTest' and having 'no bean' of type, that you can't figure out why, try @MockBean, and figure out later
    private ApplicationDao applicationDao;

    private CollegeStudent collegeStudent;

    private static String EMAIL_DOMAIN = "hotmail.com";


    @BeforeEach
    void setUp() {
        collegeStudent = CollegeStudent.builder()
                .id(1)
                .email("abg@".concat(EMAIL_DOMAIN))
                .firstName("AJ")
                .lastName("B")
//                .studentGrades(studentGrades)
                .build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testGetEmailDomainReturnsCorrectDomain() {
        Object emailDomain = ReflectionTestUtils.invokeMethod(collegeStudent, "getEmailDomain");
        Assertions.assertEquals(EMAIL_DOMAIN, emailDomain.toString());
    }


}