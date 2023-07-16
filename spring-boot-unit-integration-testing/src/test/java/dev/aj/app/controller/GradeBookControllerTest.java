package dev.aj.app.controller;

import dev.aj.app.model.CollegeStudent;
import dev.aj.app.service.StudentService;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.ModelAndView;

@AutoConfigureMockMvc
@SpringBootTest
class GradeBookControllerTest {

    public static final String EMAIL = "abg@hotmail.com";
    private static CollegeStudent collegeStudent;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @MockBean
//    @Autowired // Instead of Autowiring student service, we can Mock it as well, so real bean will be replaced
    private StudentService studentService;

    @BeforeAll
    static void beforeAll() {
        collegeStudent = CollegeStudent.builder()
                .firstName("AJ")
                .lastName("B")
                .email(EMAIL)
                .build();
    }

    @BeforeEach
    void setUp() {
//        studentService.createStudent(collegeStudent); // If mocking bean then might as well remove it.
        jdbcTemplate.execute("insert into student(first_name, last_name, email) values('AJ', 'B', 'abg@gmail.com')");
        jdbcTemplate.execute("insert into student(first_name, last_name, email) values('DJ', 'B', 'djg@gmail.com')");
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.execute("delete from student");
    }

    @Test
    void getStudents() throws Exception {

        Mockito.when(studentService.findAllStudents()).thenAnswer(this::fetchStudentsFromDB);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();

        ModelAndView modelAndView = mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(modelAndView, "index");

        List<CollegeStudent> students = (List<CollegeStudent>) modelAndView.getModel().get("students");
        Assertions.assertThat(students.size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    @Disabled("Yet to be built")
    void studentInformation() {

    }

    private List<CollegeStudent> fetchStudentsFromDB(InvocationOnMock invocation) {
        return jdbcTemplate.query("select first_name, last_name, email from student", (rs, rowNum) ->
                CollegeStudent.builder()
                        .firstName(rs.getString(1))
                        .lastName(rs.getString(2))
                        .email(rs.getString(3))
                        .build());
    }
}