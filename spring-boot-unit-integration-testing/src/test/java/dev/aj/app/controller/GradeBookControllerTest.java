package dev.aj.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.aj.app.model.CollegeStudent;
import dev.aj.app.repository.CollegeStudentRepository;
import dev.aj.app.service.StudentService;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.ModelAndView;

@AutoConfigureMockMvc
@SpringBootTest
@TestPropertySource("/application.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class GradeBookControllerTest {

    public static final String EMAIL = "abg@hotmail.com";
    public static final String FIRST_NAME = "AJ";
    public static final String LAST_NAME = "B";
    private static CollegeStudent collegeStudent;

    private static MockHttpServletRequest request;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @MockBean
//    @Autowired
    private StudentService studentService;

    @Autowired
    private CollegeStudentRepository repository;

    @BeforeAll
    static void beforeAll() {
        collegeStudent = CollegeStudent.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .build();

        request = new MockHttpServletRequest();
        request.setParameter("firstName", FIRST_NAME);
        request.setParameter("lastName", LAST_NAME);
        request.setParameter("email", EMAIL);
    }

    @BeforeEach
    void setUp() {
//        studentService.createStudent(collegeStudent); // If mocking bean then might as well remove it.
        jdbcTemplate.execute("insert into student(first_name, last_name, email) values('AJ', 'B', 'abg@gmail.com')");
        jdbcTemplate.execute("insert into student(first_name, last_name, email) values('DJ', 'B', 'djg@gmail.com')");
    }

    @AfterEach
    void tearDown() {
        //What can go wrong here? So every test will create the 2 entries in student table, and then delete all,
        //What you reckon will happen to the ID column? Be careful when using ID column in your tests, expecting it to be a certain number might not work.
        //TODO: Run by uncommenting the following line
//        jdbcTemplate.execute("delete from student");
    }

    @Test
    void Get_Students() throws Exception {

        Mockito.when(studentService.getGradebook()).thenAnswer(this::fetchStudentsFromDB);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();

        ModelAndView modelAndView = mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(modelAndView, "index");

        List<CollegeStudent> students = (List<CollegeStudent>) modelAndView.getModel().get("students");
        Assertions.assertThat(students.size()).isGreaterThanOrEqualTo(2);
    }

    private List<CollegeStudent> fetchStudentsFromDB(InvocationOnMock invocation) {
        return jdbcTemplate.query("select first_name, last_name, email from student", (rs, rowNum) ->
                CollegeStudent.builder()
                        .firstName(rs.getString(1))
                        .lastName(rs.getString(2))
                        .email(rs.getString(3))
                        .build());
    }

    @Test
    public void Create_Student_Http_Request() throws Exception {

        Mockito.when(studentService.getGradebook()).thenReturn(List.of(collegeStudent));

        org.junit.jupiter.api.Assertions.assertIterableEquals(List.of(collegeStudent), studentService.getGradebook());

        //This only comes into effect if mocking studentService, remove @Autowired
        Mockito.when(studentService.createStudent(ArgumentMatchers.any(CollegeStudent.class))).thenAnswer(invocation -> {
            CollegeStudent student = invocation.getArgument(0, CollegeStudent.class);
            student.setId(1L);
            return student;
        });

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("firstName", FIRST_NAME) // Spring requires object to be in the DTO format 'firstName', or @JsonProperty might work
                        .param("lastName", request.getParameterValues("lastName")) //Param are the 'ModelAttribute' that spring will bind to the object at controller
                        .param("email", request.getParameter("email")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

     /*   MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(collegeStudent)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();*/

        Map<String, Object> modelObject = mvcResult.getModelAndView().getModel();

        CollegeStudent savedStudent = (CollegeStudent) modelObject.get("savedStudent");

        org.junit.jupiter.api.Assertions.assertIterableEquals(List.of(collegeStudent), (List<CollegeStudent>) modelObject.get("students"));

        Assertions.assertThat(savedStudent.getId()).isGreaterThanOrEqualTo(1L);

        ModelAndViewAssert.assertViewName(mvcResult.getModelAndView(), "index");
    }

    @Test
    public void deleteStudentHttpRequest() throws Exception {

        //Note: @BeforeEach will ensure there is always a student with Id 1l present
        org.junit.jupiter.api.Assertions.assertTrue(repository.findById(1L).isPresent());

        Mockito.when(studentService.checkIfStudentExistsById(ArgumentMatchers.anyLong())).thenReturn(true);

        // When your tests are set up correctly, you can just replace @MockBean with @Autowired, and comment any relevant mocks
        Mockito.doAnswer(invocation -> {
            Long studentId = invocation.getArgument(0, Long.class);
            jdbcTemplate.execute("delete from student where id = " + studentId);
            return null;
        })
                .when(studentService)
                .deleteStudentById(1l);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/{studentId}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        ModelAndViewAssert.assertViewName(mvcResult.getModelAndView(), "index");

        org.junit.jupiter.api.Assertions.assertFalse(repository.findById(1l).isPresent());
    }

    @Test
    public void Delete_Non_Existing_Student_Http_Request() throws Exception {

        Mockito.when(studentService.checkIfStudentExistsById(ArgumentMatchers.anyLong())).thenReturn(false);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/{studentId}", 3))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();

        ModelAndViewAssert.assertViewName(mvcResult.getModelAndView(), "error");

    }
}