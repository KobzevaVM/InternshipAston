package ru.kobzeva.aston.mapper.interfaces;

import ru.kobzeva.aston.dto.*;
import ru.kobzeva.aston.entity.Book;
import ru.kobzeva.aston.entity.Person;

import java.util.List;

public interface BookDtoMapper {
    Book map(BookDtoIn bookDtoIn);

    Book map(BookDtoUpdate bookDtoUpdate);

    BookDtoOut map(Book book);

    List<BookDtoOut> map(List<Book> book);
}
