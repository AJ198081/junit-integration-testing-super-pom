package dev.aj.app.model;

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

    private List<Grade> mathGradeResults;
    private List<Grade> scienceGradeResults;
    private List<Grade> historyGradeResults;

    public double findGradePointAverage(List<Grade> grades) {
        return grades.stream()
                .mapToDouble(Grade::getGrade)
                .average().orElseGet(() -> 0.0);
    }

}
