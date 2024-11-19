package ru.hogwarts.school;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getStudentTest() throws Exception {
        Student student = new Student("Сэдрик", 15);
        student.setId(1L);

        when(studentService.getStudent(1L)).thenReturn(student);

        mockMvc.perform(get("/student/1/get"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Сэдрик"))
                .andExpect(jsonPath("$.age").value(15));
    }

    @Test
    void createStudentTest() throws Exception {
        Student student = new Student("Гермиона", 12);
        student.setId(2L);

        when(studentService.createStudent(Mockito.any(Student.class))).thenReturn(student);

        mockMvc.perform(post("/student/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Гермиона"))
                .andExpect(jsonPath("$.age").value(12));
    }

    @Test
    void updateStudentTest() throws Exception {
        Student updatedStudent = new Student("Нэвил", 13);
        updatedStudent.setId(3L);

        when(studentService.updateStudent(Mockito.any(Student.class), eq(3L))).thenReturn(updatedStudent);

        mockMvc.perform(put("/student/3/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedStudent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Нэвил"))
                .andExpect(jsonPath("$.age").value(13));
    }

    @Test
    void deleteStudentTest() throws Exception {
        mockMvc.perform(delete("/student/3/delete"))
                .andExpect(status().isOk());
    }

    @Test
    void testFilterByAge() throws Exception {
        Student student1 = new Student("Student1", 20);
        Student student2 = new Student("Student2", 20);

        when(studentService.getStudentByAge(20)).thenReturn(Arrays.asList(student1, student2));

        mockMvc.perform(get("/student/filterByAge").param("age", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Student1"))
                .andExpect(jsonPath("$[1].name").value("Student2"));
    }
}