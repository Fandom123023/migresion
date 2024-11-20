package ru.hogwarts.school.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FacultyController.class)
class FacultyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyRepository facultyRepository;

    @MockBean
    private FacultyService facultyService;

    @MockBean
    private StudentService studentService;

    @InjectMocks
    private FacultyController facultyController;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getFacultyTest() throws Exception {
        Faculty faculty = new Faculty("Гриффиндор", "Red");
        faculty.setId(1L);

        when(facultyService.getFaculty(1L)).thenReturn(faculty);

        mockMvc.perform(get("/faculty/1/get"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Гриффиндор"))
                .andExpect(jsonPath("$.color").value("Red"));
    }

    @Test
    void createFacultyTest() throws Exception {
        Faculty faculty = new Faculty("Слизерин", "Green");
        faculty.setId(2L);

        when(facultyService.createFaculty(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(post("/faculty/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Слизерин"))
                .andExpect(jsonPath("$.color").value("Green"));
    }

    @Test
    void updateFacultyTest() throws Exception {
        Faculty updatedFaculty = new Faculty("Когтевран", "Blue");
        updatedFaculty.setId(3L);

        when(facultyService.updateFaculty(any(Faculty.class), eq(3L)))
                .thenReturn(updatedFaculty);

        mockMvc.perform(put("/faculty/3/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedFaculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Когтевран"))
                .andExpect(jsonPath("$.color").value("Blue"));
    }

    @Test
    void deleteFacultyTest() throws Exception {
        mockMvc.perform(delete("/faculty/3/delete"))
                .andExpect(status().isOk());
    }

    @Test
    void filterByColorTest() throws Exception {
        Faculty faculty1 = new Faculty("Пуффендуй", "Yellow");
        Faculty faculty2 = new Faculty("Когтевран", "Blue");

        when(facultyService.getFacultiesByColor("Yellow"))
                .thenReturn(List.of(faculty1));

        mockMvc.perform(get("/faculty/filterByColor").param("color", "Yellow"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Пуффендуй"));
    }
}
