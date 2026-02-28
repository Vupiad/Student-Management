package vn.edu.hcmut.cse.adse.lab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import vn.edu.hcmut.cse.adse.lab.service.StudentService;
import vn.edu.hcmut.cse.adse.lab.entity.Student;
import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentWebController {
    @Autowired
    private StudentService service;

    // Route: GET http://localhost:8080/students
    @GetMapping
    public String getAllStudents(@RequestParam(required = false) String keyword, Model model) {
        List<Student> students;
        if (keyword != null && !keyword.isEmpty()) {
            students = service.searchByName(keyword);
        } else {
            students = service.getAll();
        }
        model.addAttribute("dsSinhVien", students);
        return "students";
    }

    // Route: GET http://localhost:8080/students/add (must be before /{id} to avoid route conflict)
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("isEdit", false);
        return "form";
    }

    // Route: GET http://localhost:8080/students/{id}
    @GetMapping("/{id}")
    public String getStudentDetail(@PathVariable String id, Model model) {
        Student student = service.getById(id);
        if (student == null) {
            return "redirect:/students";
        }
        model.addAttribute("student", student);
        return "detail";
    }

    // Route: GET http://localhost:8080/students/{id}/edit
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable String id, Model model) {
        Student student = service.getById(id);
        if (student == null) {
            return "redirect:/students";
        }
        model.addAttribute("student", student);
        model.addAttribute("isEdit", true);
        return "form";
    }

    // Route: POST http://localhost:8080/students (save new student)
    @PostMapping
    public String saveStudent(Student student, RedirectAttributes redirectAttributes) {
        service.save(student);
        redirectAttributes.addFlashAttribute("message", "Sinh viên được thêm thành công!");
        return "redirect:/students";
    }

    // Route: POST http://localhost:8080/students/{id} (update student)
    @PostMapping("/{id}")
    public String updateStudent(@PathVariable String id, Student student, RedirectAttributes redirectAttributes) {
        student.setId(id);
        service.save(student);
        redirectAttributes.addFlashAttribute("message", "Sinh viên được cập nhật thành công!");
        return "redirect:/students";
    }

    // Route: POST http://localhost:8080/students/{id}/delete (delete student)
    @PostMapping("/{id}/delete")
    public String deleteStudent(@PathVariable String id, RedirectAttributes redirectAttributes) {
        service.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Sinh viên được xóa thành công!");
        return "redirect:/students";
    }
}
