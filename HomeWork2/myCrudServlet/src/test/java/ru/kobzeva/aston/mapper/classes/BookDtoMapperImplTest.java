package ru.kobzeva.aston.mapper.classes;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.kobzeva.aston.dto.BookDtoIn;
import ru.kobzeva.aston.dto.BookDtoOut;
import ru.kobzeva.aston.dto.BookDtoUpdate;
import ru.kobzeva.aston.entity.Book;
import ru.kobzeva.aston.mapper.interfaces.BookDtoMapper;

import java.util.ArrayList;
import java.util.List;

public class BookDtoMapperImplTest {
    private BookDtoMapper bookDtoMapper;

    @BeforeEach
    void setBookDtoMapper() {
        bookDtoMapper = BookDtoMapperImpl.getBookDtoMapper();
    }

    @DisplayName(value = "Book map(BookDtoIn)")
    @Test
    public void mapDtoIn() {
        BookDtoIn bookDtoIn = new BookDtoIn(
                "BookTitleTest",
                "AuthorTest",
                1957,
                100
        );
        Book result = bookDtoMapper.map(bookDtoIn);

        Assertions.assertEquals(bookDtoIn.getBookTitle(), result.getBookTitle());
        Assertions.assertEquals(bookDtoIn.getAuthor(), result.getAuthor());
        Assertions.assertEquals(bookDtoIn.getYear(), result.getYear());
        Assertions.assertEquals(bookDtoIn.getPersonId(), result.getPersonId());
    }

    @DisplayName(value = "Book map(BookDtoUpdate)")
    @Test
    public void mapDtoUpdate() {
        BookDtoUpdate bookDtoUpdate = new BookDtoUpdate(
                1001,
                "BookTitleTest",
                "AuthorTest",
                1957,
                null
        );
        Book result = bookDtoMapper.map(bookDtoUpdate);

        Assertions.assertEquals(bookDtoUpdate.getId(), result.getId());
        Assertions.assertEquals(bookDtoUpdate.getBookTitle(), result.getBookTitle());
        Assertions.assertEquals(bookDtoUpdate.getAuthor(), result.getAuthor());
        Assertions.assertEquals(bookDtoUpdate.getYear(), result.getYear());
        Assertions.assertNull(result.getPersonId());
    }

    @DisplayName(value = "BookDtoOut map(Book)")
    @Test
    public void mapDtoOut() {
        Book book = new Book(
                100,
                "Harry Potter",
                "J. K. Rowling",
                1997,
                3
        );
        BookDtoOut result = bookDtoMapper.map(book);

        Assertions.assertEquals(book.getId(), result.getId());
        Assertions.assertEquals(book.getBookTitle(), result.getBookTitle());
        Assertions.assertEquals(book.getAuthor(), result.getAuthor());
        Assertions.assertEquals(book.getYear(), result.getYear());
        Assertions.assertEquals(book.getPersonId(), result.getPersonId());
    }

    @DisplayName(value = "List<BookDtoOut> map(List<Book>)")
    @Test
    public void mapListDtoOut() {
        List<Book> books = new ArrayList<>();
        books.add(new Book(100,
                "Harry Potter",
                "J. K. Rowling",
                1997,
                3));
        books.add(new Book(101,
                "Harry Potter 2",
                "J. K. Rowling",
                1998,
                3));
        List<BookDtoOut> result = bookDtoMapper.map(books);

        Assertions.assertEquals(books.size(), result.size());
        Assertions.assertEquals(books.get(0).getId(), result.get(0).getId());
        Assertions.assertEquals(books.get(0).getBookTitle(), result.get(0).getBookTitle());
        Assertions.assertEquals(books.get(0).getAuthor(), result.get(0).getAuthor());
        Assertions.assertEquals(books.get(0).getYear(), result.get(0).getYear());
        Assertions.assertEquals(books.get(0).getPersonId(), result.get(0).getPersonId());
        Assertions.assertEquals(books.get(1).getId(), result.get(1).getId());
        Assertions.assertEquals(books.get(1).getBookTitle(), result.get(1).getBookTitle());
        Assertions.assertEquals(books.get(1).getAuthor(), result.get(1).getAuthor());
        Assertions.assertEquals(books.get(1).getYear(), result.get(1).getYear());
        Assertions.assertEquals(books.get(1).getPersonId(), result.get(1).getPersonId());
    }
}
