package dev.aj.service;

import dev.aj.domain.model.Student;
import dev.aj.repository.StudentRepository;
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

        return optionalStudent.orElseThrow(() -> new NoSuchElementException(String.format("Unable to find student with %d", studentId)));
    }

    public Student save(Student student) {
        return studentRepository.save(student);
    }
}
