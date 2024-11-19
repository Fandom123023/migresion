package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.List;


@RestController
@RequestMapping("/faculty")
public class FacultyController {
    private final FacultyService facultyService;

    private final StudentService studentService;

    public FacultyController(FacultyService facultyService, StudentService studentService) {
        this.facultyService = facultyService;
        this.studentService = studentService;
    }

    @GetMapping("/{id}/get")
    public ResponseEntity<Faculty> getFaculty(@PathVariable Long id) {
        Faculty faculty = facultyService.getFaculty(id);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }

    @PostMapping("/create")
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return facultyService.createFaculty(faculty);
    }


    @PutMapping("/{id}/update")
    public ResponseEntity<Faculty> updateFaculty(@RequestBody Faculty faculty,
                                                 @PathVariable Long id) {
        Faculty foundFaculty = facultyService.updateFaculty(faculty, id);
        if (foundFaculty == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(foundFaculty);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/filterByColor")
    public List<Faculty> getFacultiesByColor(@RequestParam("color") String color) {
        return facultyService.getFacultiesByColor(color);
    }

    @GetMapping("/get/find-By-color-Or-Name")
    public List<Faculty> getFindByColorIgnoreCaseOrNameIgnoreCase(@RequestParam("color") String color,
                                                                  @RequestParam("Name") String Name) {
        return facultyService.findByColorIgnoreCaseOrNameIgnoreCase(color, Name);
    }

    @GetMapping("/{id}/find-faculty-by-studentid")
    public Faculty findByStudentId(@PathVariable long id) {
        return studentService.findByStudentId(id);
    }
}