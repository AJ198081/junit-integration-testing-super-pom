package dev.aj.app.repository;

import dev.aj.app.model.ScienceGrade;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScienceGradeRepository extends JpaRepository<ScienceGrade, Long> {

    Optional<ScienceGrade> findScienceGradeByStudentId(Long studentId);

}
