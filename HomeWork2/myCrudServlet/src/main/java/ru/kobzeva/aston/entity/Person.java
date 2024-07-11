package ru.kobzeva.aston.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Person {
    private Integer id;
    private String firstName;
    private String lastName;
    private Integer birthYear;
    private List<Book> books;
}
