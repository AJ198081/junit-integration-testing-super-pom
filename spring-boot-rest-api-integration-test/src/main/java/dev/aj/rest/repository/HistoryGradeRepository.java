package dev.aj.rest.repository;

import dev.aj.rest.model.HistoryGrade;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface HistoryGradeRepository extends JpaRepository<HistoryGrade, Long> {

    Optional<HistoryGrade> findHistoryGradeByStudentId(Long studentId);

    Optional<HistoryGrade> findHistoryGradeById(Long gradeId);

    void deleteAllByStudentId(Long studentId);

    List<HistoryGrade> findAllByStudentId(long studentId);
}
