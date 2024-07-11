package ru.kobzeva.aston.mapper.classes;

import ru.kobzeva.aston.dto.BookDtoIn;
import ru.kobzeva.aston.dto.BookDtoOut;
import ru.kobzeva.aston.dto.BookDtoUpdate;
import ru.kobzeva.aston.entity.Book;
import ru.kobzeva.aston.mapper.interfaces.BookDtoMapper;

import java.util.ArrayList;
import java.util.List;

public class BookDtoMapperImpl implements BookDtoMapper {
    private static BookDtoMapper bookDtoMapper;

    private BookDtoMapperImpl() {
    }

    public static synchronized BookDtoMapper getBookDtoMapper() {
        if (bookDtoMapper == null) {
            bookDtoMapper = new BookDtoMapperImpl();
        }
        return bookDtoMapper;
    }

    @Override
    public Book map(BookDtoIn bookDtoIn) {
        return new Book(
                null,
                bookDtoIn.getBookTitle(),
                bookDtoIn.getAuthor(),
                bookDtoIn.getYear(),
                bookDtoIn.getPersonId()
        );
    }

    @Override
    public Book map(BookDtoUpdate bookDtoUpdate) {
        return new Book(
                bookDtoUpdate.getId(),
                bookDtoUpdate.getBookTitle(),
                bookDtoUpdate.getAuthor(),
                bookDtoUpdate.getYear(),
                bookDtoUpdate.getPersonId()
        );
    }

    @Override
    public BookDtoOut map(Book book) {
        return new BookDtoOut(
                book.getId(),
                book.getBookTitle(),
                book.getAuthor(),
                book.getYear(),
                book.getPersonId()
        );
    }

    @Override
    public List<BookDtoOut> map(List<Book> book) {
        if (book == null) {
            return new ArrayList<>();
        }
        return book.stream().map(this::map).toList();
    }
}
