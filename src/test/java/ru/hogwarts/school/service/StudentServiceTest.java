package ru.hogwarts.school.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.hogwarts.school.model.Student;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class StudentServiceTest {
    @Autowired
    private StudentService studentService;

    @Test
    @DisplayName("Создание студента")
    void createStudent() {
        Student expected = new Student("Max", 20);

        Student actual = studentService.createStudent(expected);

        assertEquals(actual, expected);
    }


    @Test
    @DisplayName("Удаление студента")
    void deleteStudent() {
        Student expected = new Student("Serg", 35);
        Student savedStudent = studentService.createStudent(expected);

        Student actual = studentService.deleteStudent(savedStudent.getId());

        assertEquals(actual, savedStudent);
    }

    @Test
    @DisplayName("Обновление студента")
    void updateStudent() {
        Student expected = new Student("Serg", 35);
        Student savedStudent = studentService.createStudent(expected);

        Student actual = studentService.updateStudent(savedStudent, expected.getId());
        assertEquals(actual, savedStudent);
    }

    @Test
    @DisplayName("Сортировка студентов по возрасту")
    void getStudentByAge() {
        int age = 20;
        Student student = new Student("Tot", 29);
        Student expected1 = new Student("test", age);
        Student expected2 = new Student("test", age);
        studentService.createStudent(student);
        studentService.createStudent(expected1);
        studentService.createStudent(expected2);

        List<Student> actual = studentService.getStudentByAge(age);

        assertThat(actual).containsAll(List.of(expected1, expected2));
    }
}