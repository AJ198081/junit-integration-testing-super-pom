package dev.aj.service;

import dev.aj.domain.model.Student;
import dev.aj.exceptionHandlers.StudentNotFoundException;
import dev.aj.repository.StudentRepository;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

//Ensures that none of the 'web-environment' related beans are brought into context
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    public static final long STUDENT_ID = 23L;
    //    @Autowired // Technically a SpringBootTest now is just waste of resources, removing that will work fine.
    //    @MockBean
    @Mock // If not using SpringBootTest, then inject Mock rather than MockBean
    private StudentRepository studentRepository;

    //    @Autowired
    @InjectMocks // If removing SpringBootTest then just inject mocks, and save bringing application context in being
    private StudentService studentService;

    private Student student;

    private Student savedStudent;

    private Student studentFetchedFromRepository;

    @Captor // Useful when you want to ensure a lengthy method passes expected interactions with dependencies
    // Answer is generally a better option, but ArgumentCaptor might have use case, or you might come across it in legacy code
    private ArgumentCaptor<Long> longCaptor;

    @BeforeEach
    void setUp() {
        student = Student.builder()
                         .firstName("AJ")
                         .lastName("B")
                         .email("abh@gmail.com")
                         .grade(23.0)
                         .build();

//        studentFetchedFromRepository = studentService.getStudentById(savedStudent.getId());
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void Get_Student_By_Id_For_Saved_Student() {

        student.setId(STUDENT_ID);

        Mockito.when(studentRepository.save(student))
               .thenReturn(student);

        Mockito.when(studentRepository.findById(longCaptor.capture()))
               .thenReturn(Optional.ofNullable(student));

        Assertions.assertThat(student)
                  .isNotNull()
                  .isInstanceOf(Student.class);

        savedStudent = studentService.save(student);

        Assertions.assertThat(studentService.getStudentById(savedStudent.getId()))
                  .extracting(Student::getFirstName, Student::getLastName, Student::getId)
                  .contains("AJ", "B", STUDENT_ID);

        Assertions.assertThat(longCaptor.getValue())
                  .isEqualTo(student.getId());


    }

    @DisplayName("EPRT-17171 Intermediary errors are saved during pre-submission validation process")
    @Test
    void Test_Update_Existing_Student_Successfully() {

        String newEmail = "pwbw@sc.com.au";
        student.setEmail(newEmail);
        student.setId(STUDENT_ID);

        Mockito.when(studentRepository.findById(ArgumentMatchers.anyLong()))
               .thenAnswer(invocation -> {
                   Long id = invocation.getArgument(0, Long.class);
                   student.setId(id);
                   return Optional.ofNullable(student);
               });

        Mockito.when(studentRepository.updateStudentById(ArgumentMatchers.anyLong(), ArgumentMatchers.anyString()))
               .thenAnswer(invocation -> {

                   Long id = invocation.getArgument(0, Long.class);
                   String emailToBeUpdated = invocation.getArgument(1, String.class);

                   Student.builder()
                          .email(emailToBeUpdated)
                          .id(id)
                          .firstName(student.getFirstName())
                          .lastName(student.getLastName())
                          .grade(student.getGrade())
                          .build();

                   //Return number of students updated
                   return 1;
               });

        Student updatedStudent = studentService.updateStudent(STUDENT_ID, student);

        Assertions.assertThat(updatedStudent)
                  .isInstanceOf(Student.class)
                  .extracting(Student::getId, Student::getEmail)
                  .contains(STUDENT_ID, newEmail);
    }

    @Test
    void Test_Update_Throw_Error_When_Updating_Non_Existing_Student() {

        String newEmail = "pwbw@sc.com.au";
        student.setEmail(newEmail);
        student.setId(STUDENT_ID);

        org.junit.jupiter.api.Assertions.assertThrows(StudentNotFoundException.class,
                () -> studentService.updateStudent(STUDENT_ID, student),
                () -> "Updating a student that doesn't exist in DB should have thrown an error.");
    }
}