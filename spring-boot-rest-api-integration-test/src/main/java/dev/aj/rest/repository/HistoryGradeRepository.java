package rest.repository;

import rest.model.HistoryGrade;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryGradeRepository extends JpaRepository<HistoryGrade, Long> {

    Optional<HistoryGrade> findHistoryGradeByStudentId(Long studentId);

    Optional<HistoryGrade> findHistoryGradeById(Long gradeId);

    void deleteAllByStudentId(Long studentId);

    List<HistoryGrade> findAllByStudentId(long studentId);
}
