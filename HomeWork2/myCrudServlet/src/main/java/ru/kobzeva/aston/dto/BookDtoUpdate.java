package ru.kobzeva.aston.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BookDtoUpdate {
    private Integer id;
    private String bookTitle;
    private String author;
    private Integer year;
    private Integer personId;
}
