package dev.aj.rest.controller;

import dev.aj.rest.exception_handlers.StudentOrGradeErrorResponse;
import dev.aj.rest.exception_handlers.StudentOrGradeNotFoundException;
import dev.aj.rest.model.CollegeStudent;
import dev.aj.rest.model.enums.GradeType;
import dev.aj.rest.service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest")
@RequiredArgsConstructor
public class GradeBookController {

    public static final String UNABLE_TO_FIND_STUDENT_INFORMATION = "Unable to find Student Information for id: %s";
    private final StudentService studentService;

    @GetMapping(path = "/student")
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

    @PostMapping("/student")
    public CollegeStudent createStudent(@RequestBody CollegeStudent student, HttpServletRequest request, HttpServletResponse response) { // Spring will ensure that 'params' are mapped to 'student'

        String string = request.getHeader("X-Authorisation-User");

        response.addHeader("X-Authorisation-User", string + " Response");

        return studentService.createStudent(student);
    }

    @DeleteMapping(path = "/student/{id}")
    public void deleteStudent(@PathVariable Long id, HttpServletResponse response) {

        if (!studentService.checkIfStudentExistsById(id)) {
            throw new StudentOrGradeNotFoundException(
                    String.format(UNABLE_TO_FIND_STUDENT_INFORMATION, id));
        } else {
            studentService.deleteStudentById(id);
            response.addHeader("X-Deleted-Student", String.valueOf(id));
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

