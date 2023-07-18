package dev.aj.app.service;

import dev.aj.app.model.CollegeStudent;
import dev.aj.app.model.Gradebook;
import dev.aj.app.model.HistoryGrade;
import dev.aj.app.model.MathGrade;
import dev.aj.app.model.ScienceGrade;
import dev.aj.app.model.enums.GradeType;
import dev.aj.app.repository.CollegeStudentRepository;
import dev.aj.app.repository.HistoryGradeRepository;
import dev.aj.app.repository.MathGradeRepository;
import dev.aj.app.repository.ScienceGradeRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final CollegeStudentRepository studentRepository;
    private final MathGradeRepository mathGradeRepository;
    private final HistoryGradeRepository historyGradeRepository;
    private final ScienceGradeRepository scienceGradeRepository;

    public CollegeStudent createStudent(CollegeStudent collegeStudent) {
        return studentRepository.save(collegeStudent);
    }

    public CollegeStudent findStudentByEmail(String email) {
        return studentRepository.findByEmail(email)
                .orElseThrow(
                        () -> new NoSuchElementException(String.format("Unable to find student with email %s", email))
                );
    }

    public List<CollegeStudent> getGradebook() {
        return studentRepository.findAll();
    }

    public List<CollegeStudent> findAllStudents() {
        return studentRepository.findAll();
    }

    @Transactional
    public void deleteStudentById(Long studentId) {

        mathGradeRepository.deleteAllByStudentId(studentId);
        scienceGradeRepository.deleteAllByStudentId(studentId);
        historyGradeRepository.deleteAllByStudentId(studentId);

        studentRepository.deleteById(studentId);
    }

    public boolean checkIfStudentExistsById(Long studentId) {
        return studentRepository.existsById(studentId);
    }

    public boolean createGrade(double grade, long studentId, GradeType gradeType) {
        if (!checkIfStudentExistsById(studentId)) {
            return false;
        }

        if (grade >= 0 && grade <= 100) {
            switch (gradeType) {
                case MATH -> {
                    MathGrade mathGrade = MathGrade.builder()
                            .studentId(studentId)
                            .grade(grade)
                            .build();
                    MathGrade savedMathGrade = mathGradeRepository.save(mathGrade);
                    return true;
                }
                case HISTORY -> {
                    HistoryGrade historyGrade = HistoryGrade.builder()
                            .studentId(studentId)
                            .grade(grade)
                            .build();
                    HistoryGrade savedMathGrade = historyGradeRepository.save(historyGrade);
                    return true;

                }
                case SCIENCE -> {
                    ScienceGrade scienceGrade = ScienceGrade.builder()
                            .studentId(studentId)
                            .grade(grade)
                            .build();
                    ScienceGrade savedMathGrade = scienceGradeRepository.save(scienceGrade);
                    return true;
                }
                default -> {
                    return false;
                }
            }
        }
        return false;
    }

    public Long deleteGrade(Long gradeId, GradeType gradeType) {

        switch (gradeType) {
            case MATH -> {
                Optional<MathGrade> mathGradeById = mathGradeRepository.findMathGradeById(gradeId);
                Optional<Long> studentId = mathGradeById.map(MathGrade::getStudentId);
                if (mathGradeById.isPresent()) {
                    mathGradeRepository.deleteById(gradeId);
                    return studentId.orElse(0L);
                }
            }
            case SCIENCE -> {
                Optional<ScienceGrade> scienceGradeById = scienceGradeRepository.findScienceGradeById(gradeId);
                Optional<Long> studentId = scienceGradeById.map(ScienceGrade::getStudentId);
                if (scienceGradeById.isPresent()) {
                    scienceGradeRepository.deleteById(gradeId);
                    return studentId.orElse(0L);
                }
            }
            case HISTORY -> {
                Optional<HistoryGrade> historyGradeByStudentId = historyGradeRepository.findHistoryGradeById(gradeId);
                Optional<Long> studentId = historyGradeByStudentId.map(HistoryGrade::getStudentId);
                if (historyGradeByStudentId.isPresent()) {
                    historyGradeRepository.deleteById(gradeId);
                    return studentId.orElse(0L);
                }
            }
            default -> {
                return 0L;
            }
        }
        return 0L;
    }
}
