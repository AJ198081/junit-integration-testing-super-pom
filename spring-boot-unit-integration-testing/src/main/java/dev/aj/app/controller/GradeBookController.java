package dev.aj.app.controller;

import dev.aj.app.model.CollegeStudent;
import dev.aj.app.model.Gradebook;
import dev.aj.app.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class GradeBookController {

    private final Gradebook gradebook;

    private final StudentService studentService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getStudents(Model model) {

        model.addAttribute("students", studentService.getGradebook());

        return "index";
    }

    @GetMapping(path = "/studentInformation/{id}")
    public String studentInformation(@PathVariable int id, Model model) {
        return "studentInformation";
    }

    @PostMapping
    public String createStudent(@ModelAttribute("student") CollegeStudent student, Model model) { // Spring will ensure that 'params' are mapped to 'student'

        CollegeStudent savedStudent = studentService.createStudent(student);

        model.addAttribute("savedStudent", savedStudent);
        model.addAttribute("students", studentService.getGradebook());

        return "index";

    }

/*    @PostMapping
    public String createStudent(@RequestBody CollegeStudent student, Model model) { // Spring will ensure that 'params' are mapped to 'student'

        CollegeStudent savedStudent = studentService.createStudent(student);

        model.addAttribute("savedStudent", savedStudent);
        model.addAttribute("gradeBook", studentService.getGradebook());

        return "index";
    }*/
}
