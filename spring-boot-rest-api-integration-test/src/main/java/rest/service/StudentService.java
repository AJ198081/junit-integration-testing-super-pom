package rest.service;

import rest.model.CollegeStudent;
import rest.model.GradebookCollegeStudent;
import rest.model.HistoryGrade;
import rest.model.MathGrade;
import rest.model.ScienceGrade;
import rest.model.StudentGrades;
import rest.model.enums.GradeType;
import rest.repository.CollegeStudentRepository;
import rest.repository.HistoryGradeRepository;
import rest.repository.MathGradeRepository;
import rest.repository.ScienceGradeRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        if (!checkIfStudentExistsById(studentId)) return false;

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

    public GradebookCollegeStudent studentInformation(long studentId) {
        Optional<CollegeStudent> collegeStudent = studentRepository.findById(studentId);

        if (collegeStudent.isEmpty()){
            return null;
        }

        List<MathGrade> mathGrade = mathGradeRepository.findAllByStudentId(studentId);
        List<ScienceGrade> scienceGrade = scienceGradeRepository.findAllByStudentId(studentId);
        List<HistoryGrade> historyGrade = historyGradeRepository.findAllByStudentId(studentId);

        StudentGrades studentGrades = new StudentGrades(mathGrade, scienceGrade, historyGrade);

        GradebookCollegeStudent gradebookCollegeStudent = new GradebookCollegeStudent(studentGrades);

        gradebookCollegeStudent.setId(studentId);
        gradebookCollegeStudent.setFirstName(collegeStudent.get().getFirstName());
        gradebookCollegeStudent.setLastName(collegeStudent.get().getLastName());
        gradebookCollegeStudent.setEmail(collegeStudent.get().getEmail());

        return gradebookCollegeStudent;
    }

    public List<GradebookCollegeStudent> getCollegeGradebook() {
        return null;
    }
}
