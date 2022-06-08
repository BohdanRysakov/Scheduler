
Create table users
(
    id       int PRIMARY KEY AUTO_INCREMENT,
    name     varchar(30)  NOT NULL UNIQUE,
    password varchar(255) NOT NULL
);

Create table tasks
(
    idUser       int NOT NULL,
    name     varchar(30)  NOT NULL UNIQUE,
    description varchar(255) NOT NULL,
    data varchar(255) NOT NULL,
    id int NOT NULL  AUTO_INCREMENT,
    FOREIGN KEY (idUser) references users(id)

)