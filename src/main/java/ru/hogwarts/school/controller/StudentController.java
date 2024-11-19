package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/{id}/get")
    public ResponseEntity<Student> getStudent(@PathVariable Long id) {
        Student student = studentService.getStudent(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @PostMapping("/create")
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<Student> updateStudent(@RequestBody Student student,
                                                 @PathVariable Long id) {
        Student foundStudent = studentService.updateStudent(student, id);
        if (foundStudent == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(foundStudent);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/filterByAge")
    public List<Student> getStudentByAge(@RequestParam("age") int age) {
        return studentService.getStudentByAge(age);
    }

    @GetMapping("/get/between-age-max-min")
    public List<Student> getStudentByBetweenAgeMaxMin(@RequestParam int ageMin, @RequestParam int ageMax) {
        return studentService.findByAgeBetween(ageMin, ageMax);
    }

    @GetMapping("/{id}/find-students-by-facultyid")
    public List<Student> findByFacultyId(@PathVariable long id) {
        return studentService.findByFacultyId(id);
    }

    @GetMapping("/find-all-student-by-name")
    Integer getAllStudent() {
        return studentService.getAllStudent();
    }

    @GetMapping("/find-avg-students")
    Integer findAvgStudents() {
        return studentService.findAvgStudents();
    }

    @GetMapping("/find-five-last-students")
    public List<Student> findFiveLastStudents() {
        return studentService.findFiveLastStudents();
    }
}
