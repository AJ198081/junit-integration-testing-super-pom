package dev.aj.app.rest.controller;

import dev.aj.app.repository.CollegeStudentRepository;
import dev.aj.app.repository.HistoryGradeRepository;
import dev.aj.app.repository.MathGradeRepository;
import dev.aj.app.repository.ScienceGradeRepository;
import dev.aj.app.service.StudentService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class RestGradeBookControllerTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Mock
    private StudentService studentService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CollegeStudentRepository studentRepository;

    @Autowired
    private MathGradeRepository mathGradeRepository;

    @Autowired
    private ScienceGradeRepository scienceGradeRepository;

    @Autowired
    private HistoryGradeRepository historyGradeRepository;

    @Sql(scripts = "/insert_student_grades.sql")
    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.execute("delete from student");
        jdbcTemplate.execute("delete from math_grade");
        jdbcTemplate.execute("delete from history_grade");
        jdbcTemplate.execute("delete from science_grade");
    }

    @Test
    void getStudents() {
    }

    @Test
    void studentInfo() {
    }

    @Test
    void createStudent() {
    }

    @Test
    void deleteStudent() {
    }

    @Test
    void createGrade() {
    }

    @Test
    void deleteGrade() {
    }

    @Test
    void handleException() {
    }

    @Test
    void testHandleException() {
    }
}