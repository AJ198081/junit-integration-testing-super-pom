package dev.aj.repository;

import dev.aj.domain.model.Student;
import jakarta.persistence.QueryHint;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Override
    Optional<Student> findById(Long id);

    @Query("select s from Student s where s.email = ?1")
    Optional<Student> getStudentsByEmail(String email);


    @Query("select avg(s.grade) from Student s where s.active = true ")
    double findAverageGradesForActiveStudents();

    @Transactional
    @Modifying
    @QueryHints(value = {@QueryHint(name = org.hibernate.annotations.QueryHints.FLUSH_MODE, value = "COMMIT")})
    @Query(value = "update Student s set s.email = :email where s.id = :id", nativeQuery = false)
    int updateStudentById(Long id, String email);
}
