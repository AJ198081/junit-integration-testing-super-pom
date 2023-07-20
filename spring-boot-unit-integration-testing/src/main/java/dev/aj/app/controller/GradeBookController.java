package dev.aj.app.controller;

import dev.aj.app.model.CollegeStudent;
import dev.aj.app.model.Gradebook;
import dev.aj.app.model.GradebookCollegeStudent;
import dev.aj.app.model.StudentGrades;
import dev.aj.app.model.enums.GradeType;
import dev.aj.app.service.StudentService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class GradeBookController {

    public static final String INDEX = "index";

    private final StudentService studentService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getStudents(Model model) {

        model.addAttribute("students", studentService.getGradebook());

        return INDEX;
    }

    @GetMapping(path = "/studentInformation/{id}")
    public String studentInfo(@PathVariable Long id, Model model) {

        if (!studentService.checkIfStudentExistsById(id)) {
            return "error";
        }

        populateStudentInformationModel(id, model);

        return "studentInformation";
    }

    @PostMapping
    public String createStudent(@ModelAttribute("student") CollegeStudent student, Model model) { // Spring will ensure that 'params' are mapped to 'student'
//    public String createStudent(@RequestBody() CollegeStudent student, Model model) { // Spring will ensure that 'params' are mapped to 'student'

        CollegeStudent savedStudent = studentService.createStudent(student);

        model.addAttribute("savedStudent", savedStudent);
        model.addAttribute("students", studentService.getGradebook());

        return INDEX;
    }

    @GetMapping(path = "/{id}")
    public String deleteStudent(@PathVariable Long id, Model model) {

        if (!studentService.checkIfStudentExistsById(id)) {
            return "error";
        } else {
            studentService.deleteStudentById(id);
            model.addAttribute("students", studentService.getGradebook());
            return INDEX;
        }
    }

    @PostMapping(path = "/grade")
    public String createGrade(@RequestParam("grade") double grade, @RequestParam("gradeType") GradeType gradeType, @RequestParam("studentId") Long studentId, Model model) {

        if (!studentService.checkIfStudentExistsById(studentId)) {
            return "error";
        }

        boolean gradeCreated = studentService.createGrade(grade, studentId, gradeType);

        if (!gradeCreated) return "error";
        populateStudentInformationModel(studentId, model);
        return "studentInformation";
    }

    @GetMapping(path = "/grade/{id}/{gradeType}")
    public String deleteGrade(@PathVariable Long id, @PathVariable GradeType gradeType, Model model) {

        Long studentId = studentService.deleteGrade(id, gradeType);

        if (studentId == 0) return "error";

        populateStudentInformationModel(studentId, model);

        return "studentInformation";
    }

    private void populateStudentInformationModel(Long studentId, Model model) {

        GradebookCollegeStudent gradebookCollegeStudent = studentService.studentInformation(studentId);

        model.addAttribute("student", gradebookCollegeStudent);

        if (Objects.nonNull(gradebookCollegeStudent) && Objects.nonNull(gradebookCollegeStudent.getStudentGrades())) {

            StudentGrades studentGrades = Objects.requireNonNull(gradebookCollegeStudent.getStudentGrades(),
                    "StudentGrades are null");

            if (studentGrades.getMathGradeResults() != null) {
                model.addAttribute("mathAverage", studentGrades.findGradePointAverage(
                        studentGrades.getMathGradeResults()));
            } else model.addAttribute("mathAverage", "N/A");

            if (studentGrades.getScienceGradeResults() != null) {
                model.addAttribute("scienceAverage", studentGrades.findGradePointAverage(
                        studentGrades.getScienceGradeResults()));
            } else {
                model.addAttribute("scienceAverage", "N/A");
            }

            if (studentGrades.getHistoryGradeResults() != null) {
                model.addAttribute("historyAverage", studentGrades.findGradePointAverage(
                        studentGrades.getHistoryGradeResults()));
            } else {
                model.addAttribute("historyAverage", "N/A");
            }
        }
    }

}
