package ru.kobzeva.aston.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PersonDtoUpdate {
    private Integer id;
    private String firstName;
    private String lastName;
    private Integer birthYear;
}
