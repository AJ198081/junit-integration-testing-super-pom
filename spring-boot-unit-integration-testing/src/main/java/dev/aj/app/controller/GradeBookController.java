package dev.aj.app.controller;

import dev.aj.app.model.Gradebook;
import dev.aj.app.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class GradeBookController {

    @Autowired
    private Gradebook gradebook;

    @Autowired
    private StudentService studentService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getStudents(Model model) {

        model.addAttribute("students", studentService.findAllStudents());

        return "index";
    }

    @GetMapping(path = "/studentInformation/{id}")
    public String studentInformation(@PathVariable int id, Model model) {
        return "studentInformation";
    }
}
