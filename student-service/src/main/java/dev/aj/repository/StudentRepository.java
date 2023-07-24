package dev.aj.repository;

import dev.aj.domain.model.Student;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Override
    Optional<Student> findById(Long id);

    @Query("select s from Student s where s.email = ?1")
    Optional<Student> getStudentsByEmail(String email);


    @Query("select avg(s.grade) from Student s where s.active = true ")
    double findAverageGradesForActiveStudents();
}
