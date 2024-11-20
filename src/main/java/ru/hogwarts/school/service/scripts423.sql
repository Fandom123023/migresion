1.
select student.name, student.age, faculty.name as faculty_name
from student
join faculty on student.faculty_id = faculty_id;

2.
select student.name, student.age
from student
join avatar on student.id = avatar.student_id;