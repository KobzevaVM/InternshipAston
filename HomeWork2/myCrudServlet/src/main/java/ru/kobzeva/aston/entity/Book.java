package ru.kobzeva.aston.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Book {
    private Integer id;
    private String bookTitle;
    private String author;
    private Integer year;
    private Integer personId;
}
