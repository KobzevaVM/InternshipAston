package ru.kobzeva.aston.service.interfaces;

import ru.kobzeva.aston.dto.BookDtoIn;
import ru.kobzeva.aston.dto.BookDtoOut;
import ru.kobzeva.aston.dto.BookDtoUpdate;
import ru.kobzeva.aston.entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    BookDtoOut create(BookDtoIn bookDtoIn);

    BookDtoOut read(Integer BookId);

    boolean update(BookDtoUpdate bookDtoUpdate);

    boolean delete(Integer BookId);

    List<BookDtoOut> findAllBookByPersonId(Integer id);
}
