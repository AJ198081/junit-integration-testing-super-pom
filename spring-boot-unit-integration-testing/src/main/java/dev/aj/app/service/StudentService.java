package dev.aj.app.service;

import dev.aj.app.model.CollegeStudent;
import dev.aj.app.repository.CollegeStudentRepository;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final CollegeStudentRepository studentRepository;

    public CollegeStudent createStudent(CollegeStudent collegeStudent) {
        return studentRepository.save(collegeStudent);
    }

    public CollegeStudent findStudentByEmail(String email) {
        return studentRepository.findByEmail(email)
                .orElseThrow(
                        () -> new NoSuchElementException(String.format("Unable to find student with email %s", email))
                );
    }

    public List<CollegeStudent> getGradebook() {
        return studentRepository.findAll();
    }

    public List<CollegeStudent> findAllStudents() {
        return studentRepository.findAll();
    }
}
