package dev.aj.app.repository;

import dev.aj.app.model.HistoryGrade;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryGradeRepository extends JpaRepository<HistoryGrade, Long> {

    Optional<HistoryGrade> findHistoryGradeByStudentId(Long studentId);

    Optional<HistoryGrade> findHistoryGradeById(Long gradeId);

    void deleteAllByStudentId(Long studentId);
}
