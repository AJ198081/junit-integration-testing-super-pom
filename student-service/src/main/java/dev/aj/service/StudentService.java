package dev.aj.service;

import dev.aj.domain.model.Student;
import dev.aj.exceptionHandlers.StudentNotFoundException;
import dev.aj.repository.StudentRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    @Cacheable("students")
    public Student getStudentById(Long studentId) {
        Optional<Student> optionalStudent = studentRepository.findById(studentId);

        return optionalStudent.get();

//        return optionalStudent.orElseThrow(() -> new NoSuchElementException(String.format("Unable to find student with %d", studentId)));
    }

    public Student save(Student student) {
        return studentRepository.save(student);
    }

    public Student updateStudent(Long id, Student student) {
        Optional<Student> optionalStudent = studentRepository.findById(id);

        Student studentFromDB = optionalStudent.orElseThrow(
                () -> new StudentNotFoundException(String.format("Unable to find student with ID %s", id)));

        int numberOfRowsUpdated = studentRepository.updateStudentById(id, student.getEmail());

        if (numberOfRowsUpdated == 1) {
            return studentRepository.findById(id)
                                    .orElseThrow(() -> new NoSuchElementException("Something went wrong!"));
        } else {
            throw new StudentNotFoundException("Something went wrong with student Id " + id);
        }
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentByIdDangerous(Long studentId) {
        return studentRepository.findById(studentId);
    }
}
