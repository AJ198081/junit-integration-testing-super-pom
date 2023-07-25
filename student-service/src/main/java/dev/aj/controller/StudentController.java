package dev.aj.controller;

import dev.aj.domain.model.Student;
import dev.aj.exceptionHandlers.StudentNotFoundException;
import dev.aj.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    void notFound(StudentNotFoundException exception) {

    }

    @GetMapping(path = "/{studentId}")
    public ResponseEntity<Student> getStudent(@PathVariable Long studentId) {
        Student student = studentService.getStudentById(studentId);

//        Optional<Student> studentByIdDangerous = studentService.getStudentByIdDangerous(studentId);

//        Student studentOrElse = studentByIdDangerous.orElseThrow(
//                () -> new StudentNotFoundException("Unable to find student with ID " + studentId));

        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @Valid @RequestBody Student student) {
        Student updatedStudent = studentService.updateStudent(id, student);
        updatedStudent.setEmail(student.getEmail());
        return new ResponseEntity<>(updatedStudent, HttpStatus.OK);
    }
}
