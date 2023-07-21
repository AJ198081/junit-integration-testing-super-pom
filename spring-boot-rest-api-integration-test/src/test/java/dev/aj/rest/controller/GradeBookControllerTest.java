package dev.aj.rest.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.aj.rest.model.CollegeStudent;
import dev.aj.rest.repository.CollegeStudentRepository;
import dev.aj.rest.repository.HistoryGradeRepository;
import dev.aj.rest.repository.MathGradeRepository;
import dev.aj.rest.repository.ScienceGradeRepository;
import dev.aj.rest.service.StudentService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class GradeBookControllerTest {

    private static final MediaType APPLICATION_JSON = MediaType.APPLICATION_JSON;
    private static MockHttpServletRequest request;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
//    @Mock
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

    @BeforeAll
    static void beforeAll() {
        request = new MockHttpServletRequest();
        request.setParameter("firstname", "AJ");
        request.setParameter("lastname", "B");
        request.setParameter("email", "abg@gmail.com");
    }

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

    @Sql(scripts = "/insert_student_grades.sql")
    @Test
    @Order(1)
    void getStudents() throws Exception {
        List<CollegeStudent> students = jdbcTemplate.query("select id, first_name, last_name, email from student",
                (rs, rownnum) -> {
                    return CollegeStudent.builder()
                                         .id(rs.getLong(1))
                                         .firstName(rs.getString(2))
                                         .lastName(rs.getString(3))
                                         .email(rs.getString(4))
                                         .build();
                });


        students.forEach(System.out::println);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/rest/student"))
                                     .andExpect(MockMvcResultMatchers.status()
                                                                     .is2xxSuccessful())
                                     .andExpectAll(jsonPath("$", hasSize(2)))
                                     .andReturn();

        List<CollegeStudent> listOfStudents = objectMapper.readValue(mvcResult.getResponse()
                                                                              .getContentAsString(),
                new TypeReference<List<CollegeStudent>>() {
                });

        for (CollegeStudent listOfStudent : listOfStudents) {
            System.out.println(listOfStudent);
        }

    }

    @Sql("/insert_student.sql")
    @Test
    void studentInfo() throws Exception {
        List<Long> studentIds = jdbcTemplate.query("select id from student", (rs, rowNum) -> {
            return rs.getLong(1);
        });

        long firstStudentId = studentIds.stream()
                                        .mapToLong(Long::longValue)
                                        .min()
                                        .stream()
                                        .findFirst()
                                        .orElseThrow(() -> new NoSuchElementException("No Ids found"));

        MvcResult mvcResult = mockMvc.perform(
                                             MockMvcRequestBuilders.get("/rest/studentInformation/{id}", firstStudentId))
                                     .andExpectAll(MockMvcResultMatchers.status()
                                                                        .isOk(),
                                             jsonPath("$.fullName", is("AJ B")),
                                             jsonPath("$.firstName", is("AJ")),
                                             jsonPath("$.lastName", is("B")),
                                             jsonPath("$.email", is("abg@hotmail.com"))
                                     )
                                     .andReturn();

        String contentAsString = mvcResult.getResponse()
                                          .getContentAsString();
    }

    @Sql("/insert_student.sql")
    @Test
    void createStudent() throws Exception {

        CollegeStudent newStudent = CollegeStudent.builder()
                                                  .firstName("DJ")
                                                  .lastName("B")
                                                  .email("djb@gmail.com")
                                                  .build();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Authorisation-User", "AjB");
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/rest/student")
                                                                    .headers(headers)
                                                                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                                    .content(objectMapper.writeValueAsString(
                                                                            newStudent)))
                                     .andExpectAll(MockMvcResultMatchers.status()
                                                                        .isOk(),
                                             jsonPath("$.firstName", is("DJ")),
                                             MockMvcResultMatchers.header()
                                                                  .string("X-Authorisation-User", is("AjB Response")))
                                     .andReturn();

        CollegeStudent collegeStudent = objectMapper.readValue(mvcResult.getResponse()
                                                                        .getContentAsString(), CollegeStudent.class);
        System.out.println("Created Student is " + collegeStudent);
    }

    @Sql("/insert_student.sql")
    @Test
    void deleteStudent() throws Exception {
        assertTrue(studentRepository.existsById(1L));

        mockMvc.perform(MockMvcRequestBuilders.delete("/rest/student/{id}", 1))
               .andExpectAll(MockMvcResultMatchers.status()
                                                  .isOk(),
                       MockMvcResultMatchers.header()
                                            .string("X-Deleted-Student", is("1")));
    }
}