drop table if exists Person;
drop table if exists Book;


create table Person (
                        id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                        FIRST_NAME varchar(255) NOT NULL,
                        LAST_NAME varchar(255) NOT NULL,
                        BIRTH_YEAR int NOT NULL CHECK (BIRTH_YEAR > 1900)
);

insert into Person (FIRST_NAME, LAST_NAME, BIRTH_YEAR) values('Семён', 'Семёнов', 1995);
insert into Person (FIRST_NAME, LAST_NAME, BIRTH_YEAR) values('Кирилл', 'Кириллов', 1997);
insert into Person (FIRST_NAME, LAST_NAME, BIRTH_YEAR) values('Иван', 'Иванов', 2003);
insert into Person (FIRST_NAME, LAST_NAME, BIRTH_YEAR) values('Пётр', 'Петров', 1970);
insert into Person (FIRST_NAME, LAST_NAME, BIRTH_YEAR) values('Сергей', 'Сергеев', 1988);
insert into Person (FIRST_NAME, LAST_NAME, BIRTH_YEAR) values('Ольга', 'Чернова', 2000);
insert into Person (FIRST_NAME, LAST_NAME, BIRTH_YEAR) values('Анна', 'Белова', 1991);


create table book (
                      ID int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                      BOOK_TITLE varchar(255) NOT NULL,
                      AUTHOR varchar(255) NOT NULL,
                      YEAR int not null check ( year > 0 ),
                      PERSON_ID int,
                      FOREIGN KEY (PERSON_ID)  REFERENCES Person (Id) ON DELETE SET NULL
);
insert into book (BOOK_TITLE, AUTHOR, YEAR) values('Ася','Тургенев Иван', 1858);
insert into book (BOOK_TITLE, AUTHOR, YEAR) values('Ревизор','Гоголь Николай', 1836);
insert into book (BOOK_TITLE, AUTHOR, YEAR) values('Три товарища','Ремарк Эрих Мария', 1936);
insert into book (BOOK_TITLE, AUTHOR, YEAR, PERSON_ID) values('Гарри Поттер и философский камень','Дж. К. Роулинг', 1997, 1);
insert into book (BOOK_TITLE, AUTHOR, YEAR, PERSON_ID) values('Гарри Поттер и Тайная комната','Дж. К. Роулинг', 1998, 3);
insert into book (BOOK_TITLE, AUTHOR, YEAR, PERSON_ID) values('Гарри Поттер и узник Азкабана','Дж. К. Роулинг', 1999, 3);
insert into book (BOOK_TITLE, AUTHOR, YEAR) values('Гарри Поттер и Кубок огня', 'Дж. К. Роулинг', 2000);