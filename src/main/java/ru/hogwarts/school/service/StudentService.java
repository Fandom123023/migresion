package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    public StudentService(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    public Student createStudent(Student student) {
        logger.info("Метод создания студента");
        logger.debug("Создание студента с именем: " + student.getName());
        studentRepository.save(student);
        return student;
    }

    public Student getStudent(long id) {
        logger.info("Метод показывающий студента");
        return studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
    }

    public Student updateStudent(Student student, Long id) {
        logger.info("Метод для обновления студента");
        if (!studentRepository.existsById(id)) {
            logger.warn("Нет такого студента по id: " + id);
            throw new StudentNotFoundException(id);
        }
        logger.debug("Обновление студента " + student.getName() + " с " + id);
        student.setId(id);
        studentRepository.save(student);
        return student;
    }

    public Student deleteStudent(long id) {
        logger.info("Метод удаления студента");
        Student student = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
        studentRepository.delete(student);
        logger.debug("Дебаг удаления студента по id " + id);
        return student;
    }

    public List<Student> getStudentByAge(int age) {
        logger.info("Сортируем студентов по возрасту");
        return studentRepository.findAll().stream()
                .filter(student -> student.getAge() == age)
                .collect(Collectors.toList());
    }

    public List<Student> findByAgeBetween(int ageMin, int ageMax) {
        logger.info("Возраст между минимумом и максимом");
        return studentRepository.findByAgeBetween(ageMin, ageMax);
    }

    public List<Student> findByFacultyId(long id) {
        logger.info("Находим студента по факультету");
        return studentRepository.findByFacultyId(id);
    }

    public Faculty findByStudentId(long id) {
        logger.info("Находим факультет по студенту");
        return studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id)).getFaculty();
    }

    public Integer getAllStudent() {
        logger.info("Находим всем студентов");
        return studentRepository.findByAllStudentName();
    }

    public Integer findAvgStudents() {
        logger.info("Находим средний возраст студентов");
        return studentRepository.findAvgStudents();
    }

    public List<Student> findFiveLastStudents() {
        logger.info("Находим пять последних студентов");
        return studentRepository.findFiveLastStudents();
    }
}