package ru.hogwarts.school.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FacultyServiceTest {

    @Autowired
    private FacultyService facultyService;

    @Autowired
    private FacultyRepository facultyRepository;

    @Test
    @DisplayName("Создание факультета")
    void createFaculty() {
        Faculty expected = new Faculty("Max", "синий");

        Faculty actual = facultyService.createFaculty(expected);

        assertEquals(actual, expected);
    }

    @Test
    @DisplayName("Обновление факультета")
    void updateFaculty() {
        Faculty expected = new Faculty("Serg", "Зеленый");
        Faculty savedFaculty = facultyService.createFaculty(expected);

        Faculty actual = facultyService.updateFaculty(savedFaculty, expected.getId());
        assertEquals(actual, savedFaculty);
    }


    @Test
    @DisplayName("Удаление факультета")
    void deleteFaculty() {
        Faculty expected = new Faculty("Serg", "Красный");
        Faculty savedFaculty = facultyService.createFaculty(expected);

        Faculty actual = facultyService.deleteFaculty(savedFaculty.getId());

        assertEquals(actual, savedFaculty);
    }

    @Test
    @DisplayName("Сортировка по цвету")
    void getFacultiesByColor() {
        String color = "Blue";
        Faculty faculty = new Faculty("Tot", "Red");
        Faculty expected1 = new Faculty("test", color);
        Faculty expected2 = new Faculty("test", color);
        facultyService.createFaculty(faculty);
        facultyService.createFaculty(expected1);
        facultyService.createFaculty(expected2);

        List<Faculty> actual = facultyService.getFacultiesByColor(color);

        assertThat(actual).containsAll(List.of(expected1, expected2));

    }
}