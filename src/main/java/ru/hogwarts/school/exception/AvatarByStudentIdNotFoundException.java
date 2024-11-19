package ru.hogwarts.school.exception;

public class AvatarByStudentIdNotFoundException extends RuntimeException{
    public AvatarByStudentIdNotFoundException(Long id) {
        super("ID студента не найден");
    }
}