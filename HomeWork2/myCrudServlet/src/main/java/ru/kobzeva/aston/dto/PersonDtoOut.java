package ru.kobzeva.aston.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PersonDtoOut {
    private Integer id;
    private String firstName;
    private String lastName;
    private Integer birthYear;
    private List<BookDtoOut> books;
}
