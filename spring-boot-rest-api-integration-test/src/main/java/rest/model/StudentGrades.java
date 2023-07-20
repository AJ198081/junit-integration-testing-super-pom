package rest.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class StudentGrades {

    private List<MathGrade> mathGradeResults;
    private List<ScienceGrade> scienceGradeResults;
    private List<HistoryGrade> historyGradeResults;

    public double findGradePointAverage(List<? extends Grade> grades) {
        return grades.stream()
                .mapToDouble(Grade::getGrade)
                .average().orElseGet(() -> 0.0);
    }

}
