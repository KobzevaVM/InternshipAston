package ru.kobzeva.aston.repository.classes;

import org.junit.jupiter.api.*;
import ru.kobzeva.aston.entity.Book;
import ru.kobzeva.aston.repository.interfaces.BookRepository;

import java.util.Optional;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookRepositoryImplTest {
    public static BookRepository bookRepository = BookRepositoryImpl.getBookRepository();
    public static Integer newId;
    public static String testBookTitle = "update bookTitle";
    public static String testAuthor = "update author";
    public static Integer testYear = 2000;

    @Test
    @Order(1)
    public void create() {
        String bookTitle = "new bookTitle";
        String author = "new author";
        Integer year = 1803;

        Book book = new Book(
                null,
                bookTitle,
                author,
                year,
                null);
        book = bookRepository.create(book);
        Optional<Book> result = bookRepository.read(book.getId());
        result.ifPresent(value -> newId = value.getId());

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(bookTitle, result.get().getBookTitle());
        Assertions.assertEquals(author, result.get().getAuthor());
        Assertions.assertEquals(year, result.get().getYear());
    }

    @Test
    @Order(2)
    public void update() {
        Book book = bookRepository.read(newId).get();

        Assertions.assertNotEquals(testBookTitle, book.getBookTitle());
        Assertions.assertNotEquals(testAuthor, book.getAuthor());
        Assertions.assertNotEquals(testYear, book.getYear());

        book.setBookTitle(testBookTitle);
        book.setAuthor(testAuthor);
        book.setYear(testYear);
        bookRepository.update(book);

        Book result = bookRepository.read(newId).get();

        Assertions.assertEquals(testBookTitle, result.getBookTitle());
        Assertions.assertEquals(testAuthor, result.getAuthor());
        Assertions.assertEquals(testYear, result.getYear());
    }

    @Test
    @Order(3)
    public void read() {

        Book result = bookRepository.read(newId).get();

        Assertions.assertEquals(testBookTitle, result.getBookTitle());
        Assertions.assertEquals(testAuthor, result.getAuthor());
        Assertions.assertEquals(testYear, result.getYear());
    }

    @Test
    @Order(5)
    public void delete() {

        boolean result = bookRepository.delete(newId);

        Assertions.assertEquals(true, result);
    }
}
