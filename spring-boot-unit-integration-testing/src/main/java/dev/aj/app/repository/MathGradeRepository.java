package dev.aj.app.repository;

import dev.aj.app.model.MathGrade;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MathGradeRepository extends JpaRepository<MathGrade, Long> {

    Optional<MathGrade> findMathGradeByStudentId(Long studentId);

}
