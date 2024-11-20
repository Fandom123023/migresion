-- liquibase formatted sql

-- changeset asdt:1
CREATE INDEX index_student_name ON student(name);

-- changeset asdt:2
CREATE INDEX index_faculty_name_color ON faculty(name, color);