package rest.controller;

import rest.exception_handlers.StudentOrGradeErrorResponse;
import rest.exception_handlers.StudentOrGradeNotFoundException;
import rest.model.CollegeStudent;
import rest.model.enums.GradeType;
import rest.service.StudentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest")
@RequiredArgsConstructor
public class GradeBookController {

    public static final String UNABLE_TO_FIND_STUDENT_INFORMATION = "Unable to find Student Information for id: %s";
    private final StudentService studentService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<CollegeStudent> getStudents() {
        return studentService.getGradebook();
    }

    @GetMapping(path = "/studentInformation/{id}")
    public CollegeStudent studentInfo(@PathVariable Long id) {

        if (!studentService.checkIfStudentExistsById(id)) {
            throw new StudentOrGradeNotFoundException(
                    String.format(UNABLE_TO_FIND_STUDENT_INFORMATION, id));
        }

        return studentService.studentInformation(id);
    }

    @PostMapping
    public CollegeStudent createStudent(@RequestBody CollegeStudent student) { // Spring will ensure that 'params' are mapped to 'student'

        return studentService.createStudent(student);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteStudent(@PathVariable Long id) {

        if (!studentService.checkIfStudentExistsById(id)) {
            throw new StudentOrGradeNotFoundException(
                    String.format(UNABLE_TO_FIND_STUDENT_INFORMATION, id));
        } else {
            studentService.deleteStudentById(id);
        }
    }

    @PostMapping(path = "/grade")
    public CollegeStudent createGrade(@RequestParam("grade") double grade,
                                      @RequestParam("gradeType") GradeType gradeType,
                                      @RequestParam("studentId") Long studentId) {

        if (!studentService.checkIfStudentExistsById(studentId))
            throw new StudentOrGradeNotFoundException(
                    String.format(UNABLE_TO_FIND_STUDENT_INFORMATION, studentId));

        boolean gradeCreated = studentService.createGrade(grade, studentId, gradeType);

        if (!gradeCreated) throw new StudentOrGradeNotFoundException(
                String.format(UNABLE_TO_FIND_STUDENT_INFORMATION, studentId));

        return studentService.studentInformation(studentId);
    }

    @DeleteMapping(path = "/grade/{id}/{gradeType}")
    public void deleteGrade(@PathVariable Long id, @PathVariable GradeType gradeType, Model model) {

        Long studentId = studentService.deleteGrade(id, gradeType);

        if (studentId == 0) {
            throw new StudentOrGradeNotFoundException(
                    String.format(UNABLE_TO_FIND_STUDENT_INFORMATION, studentId));
        }
    }

    @ExceptionHandler
    public ResponseEntity<StudentOrGradeErrorResponse> handleException(StudentOrGradeNotFoundException exc) {

        StudentOrGradeErrorResponse error = new StudentOrGradeErrorResponse();

        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<StudentOrGradeErrorResponse> handleException(Exception exc) {

        StudentOrGradeErrorResponse error = new StudentOrGradeErrorResponse();

        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}

