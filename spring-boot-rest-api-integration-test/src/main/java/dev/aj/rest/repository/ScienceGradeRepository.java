package dev.aj.rest.repository;

import dev.aj.rest.model.ScienceGrade;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScienceGradeRepository extends JpaRepository<ScienceGrade, Long> {

    Optional<ScienceGrade> findScienceGradeByStudentId(Long studentId);

    Optional<ScienceGrade> findScienceGradeById(Long gradeId);

    void deleteAllByStudentId(Long studentId);

    List<ScienceGrade> findAllByStudentId(long studentId);
}
