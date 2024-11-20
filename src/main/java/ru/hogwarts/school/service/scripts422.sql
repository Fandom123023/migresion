create table people(
id serial primary key,
name varchar(100) not null,
age integer not null,
license BOOLEAN not null
);

create table cars(
id serial primary key,
brand varchar(100) not null,
model varchar(100) not null,
price decimal(10,2) not null
);

create table people_cars(
person_id primary key,
cars_id primary key,
primary key(people_id, cars_id),
foreign key(person_id) references people(id) on delete cascade,
foreign key(cars_id) references cars(id) on delete cascade
);