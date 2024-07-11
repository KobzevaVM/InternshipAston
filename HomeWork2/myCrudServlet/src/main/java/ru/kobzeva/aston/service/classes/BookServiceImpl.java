package ru.kobzeva.aston.service.classes;

import ru.kobzeva.aston.dto.BookDtoIn;
import ru.kobzeva.aston.dto.BookDtoOut;
import ru.kobzeva.aston.dto.BookDtoUpdate;
import ru.kobzeva.aston.entity.Book;
import ru.kobzeva.aston.mapper.classes.BookDtoMapperImpl;
import ru.kobzeva.aston.mapper.interfaces.BookDtoMapper;
import ru.kobzeva.aston.repository.classes.BookRepositoryImpl;
import ru.kobzeva.aston.repository.interfaces.BookRepository;
import ru.kobzeva.aston.service.interfaces.BookService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository = BookRepositoryImpl.getBookRepository();
    private static final BookDtoMapper bookDtoMapper = BookDtoMapperImpl.getBookDtoMapper();
    private static BookService bookService;

    private BookServiceImpl() {
    }

    public static synchronized BookService getBookService() {
        if (bookService == null) {
            bookService = new BookServiceImpl();
        }
        return bookService;
    }

    private void checkBook(Integer bookId) throws RuntimeException {
        if (bookRepository.read(bookId).isEmpty()) {
            throw new RuntimeException("Book not found");
        }
    }

    @Override
    public BookDtoOut create(BookDtoIn bookDtoIn) {
        Book book = bookDtoMapper.map(bookDtoIn);
        book = bookRepository.create(book);
        return bookDtoMapper.map(book);
    }

    @Override
    public BookDtoOut read(Integer bookId) {
        Book book = bookRepository.read(bookId).orElseThrow(() -> new RuntimeException("Book not found"));
        return bookDtoMapper.map(book);
    }

    @Override
    public boolean update(BookDtoUpdate bookDtoUpdate) {
        checkBook(bookDtoUpdate.getId());
        Book book = bookDtoMapper.map(bookDtoUpdate);
        return bookRepository.update(book);
    }

    @Override
    public boolean delete(Integer bookId) {
        checkBook(bookId);
        return bookRepository.delete(bookId);
    }

    @Override
    public List<BookDtoOut> findAllBookByPersonId(Integer personId) {
        Optional<List<Book>> books = bookRepository.findAllBookByPersonId(personId);
        return books.map(bookList -> bookDtoMapper.map(new ArrayList<>(bookList))).orElse(null);
    }
}
