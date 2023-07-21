package dev.aj.rest.repository;

import dev.aj.rest.model.MathGrade;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MathGradeRepository extends JpaRepository<MathGrade, Long> {

    Optional<MathGrade> findMathGradeByStudentId(Long studentId);

    Optional<MathGrade> findMathGradeById(Long gradeId);

    void deleteAllByStudentId(Long studentId);

    Optional<MathGrade> findMathGradesByStudentId(long studentId);

    List<MathGrade> findAllByStudentId(long studentId);
}
