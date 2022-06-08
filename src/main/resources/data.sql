
create table users(
    id int PRIMARY KEY AUTO_INCREMENT,
    name varchar(20) NOT NULL UNIQUE,
    password varchar(20) NOT NULL
);

Insert into users(name,password) values
    ('Cezar','password'),
    ('Joseph','password'),
    ('LizaLiza','password');