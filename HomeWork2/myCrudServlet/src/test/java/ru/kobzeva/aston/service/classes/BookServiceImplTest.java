package ru.kobzeva.aston.service.classes;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import ru.kobzeva.aston.dto.BookDtoIn;
import ru.kobzeva.aston.dto.BookDtoOut;
import ru.kobzeva.aston.dto.BookDtoUpdate;
import ru.kobzeva.aston.entity.Book;
import ru.kobzeva.aston.repository.classes.BookRepositoryImpl;
import ru.kobzeva.aston.repository.interfaces.BookRepository;
import ru.kobzeva.aston.service.interfaces.BookService;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookServiceImplTest {
    private static BookService bookService;
    private static BookRepository mockBookRepository;
    private static BookRepositoryImpl realBookRepository;

    private static void setMock(BookRepository mock) {
        try {
            // Получили доступ к нашему private репозиторию
            Field bookRepository = BookRepositoryImpl.class.getDeclaredField("bookRepository");
            //Даем доступ для работы с нашим private репозиторием
            bookRepository.setAccessible(true);
            //Получаем значение нашего private репозитория, чтобы вернуть в конце теста все обратно
            realBookRepository = (BookRepositoryImpl) bookRepository.get(bookRepository);
            //кладем вместо нашего реального репозитория Mock
            bookRepository.set(bookRepository, mock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //Тестируем сервис, поэтому в сервисе надо заменить реальный репозиторий на Mock,
    //чтобы при вызове методов не писал к нам в БД
    @BeforeAll
    static void replaceRepository() {
        mockBookRepository = Mockito.mock(BookRepository.class);
        setMock(mockBookRepository);
        bookService = BookServiceImpl.getBookService();
    }

    // После тестирования возвращаем в наше private поле ссылку на реальный репозиторий
    @AfterAll
    static void revertRepository() throws Exception {
        Field bookRepository = BookRepositoryImpl.class.getDeclaredField("bookRepository");
        bookRepository.setAccessible(true);
        bookRepository.set(bookRepository, realBookRepository);
    }

    @BeforeEach
    public void mockReset() {
        Mockito.reset(mockBookRepository);
    }

    @Test
    public void create() {
        Integer id = 1;

        BookDtoIn bookDtoIn = new BookDtoIn(
                "bookTitleTest",
                "authorTest",
                1803,
                null);
        Book book = new Book(
                id,
                "bookTitleTest",
                "authorTest",
                1803,
                null
        );

        //В методе create в Service вызывается метод create у Repository, который возвращает Book.
        //Сказали ему какую книгу возвращать, т.к. это Mock.
        Mockito.doReturn(book).when(mockBookRepository).create(Mockito.any(Book.class));

        BookDtoOut result = bookService.create(bookDtoIn);

        Assertions.assertEquals(id, result.getId());
        Assertions.assertEquals(bookDtoIn.getBookTitle(), result.getBookTitle());
        Assertions.assertEquals(bookDtoIn.getAuthor(), result.getAuthor());
        Assertions.assertEquals(bookDtoIn.getYear(), result.getYear());
        Assertions.assertNull(result.getPersonId());
    }

    @Test
    public void read() {
        Integer id = 1;


        Optional<Book> book = Optional.of(
                new Book(
                        id,
                        "bookTitleTest",
                        "authorTest",
                        1803,
                        null
                )
        );

        //В методе create в Service вызывается метод create у Repository, который возвращает Optional<Book>.
        //Сказали ему какую книгу(Optional<Book>) возвращать, т.к. это Mock.
        Mockito.doReturn(book).when(mockBookRepository).read(Mockito.anyInt());

        BookDtoOut result = bookService.read(1);

        Assertions.assertEquals(id, result.getId());
        Assertions.assertEquals(book.get().getBookTitle(), result.getBookTitle());
        Assertions.assertEquals(book.get().getAuthor(), result.getAuthor());
        Assertions.assertEquals(book.get().getYear(), result.getYear());
        Assertions.assertNull(result.getPersonId());
    }

    @Test
    public void readBookNotFound() {
        Optional<Book> book = Optional.empty();

        Mockito.doReturn(book).when(mockBookRepository).read(Mockito.anyInt());

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> bookService.read(1));
        Assertions.assertEquals("Book not found", exception.getMessage());
    }

    @Test
    public void update() {
        Integer id = 1;

        BookDtoUpdate bookDtoUpdate = new BookDtoUpdate(
                id,
                "bookTitleTest",
                "authorTest",
                1803,
                null
        );

        Optional<Book> book = Optional.of(
                new Book(
                        id,
                        "bookTitleTest",
                        "authorTest",
                        1803,
                        null
                )
        );

        Mockito.doReturn(true).when(mockBookRepository).update(Mockito.any(Book.class));
        Mockito.doReturn(book).when(mockBookRepository).read(Mockito.anyInt());

        boolean result = bookService.update(bookDtoUpdate);

        Assertions.assertTrue(result);
    }

    @Test
    public void updateBookNotFound() {

        BookDtoUpdate bookDtoUpdate = new BookDtoUpdate(
                null,
                "bookTitleTest",
                "authorTest",
                1803,
                null
        );

        RuntimeException exception = Assertions.assertThrows(
                RuntimeException.class,
                () -> bookService.update(bookDtoUpdate)
        );
        Assertions.assertEquals("Book not found", exception.getMessage());
    }

    @Test
    public void delete() {
        Integer id = 1;

        Optional<Book> book = Optional.of(
                new Book(
                        id,
                        "bookTitleTest",
                        "authorTest",
                        1803,
                        null
                )
        );

        Mockito.doReturn(true).when(mockBookRepository).delete(Mockito.anyInt());
        Mockito.doReturn(book).when(mockBookRepository).read(Mockito.anyInt());

        boolean result = bookService.delete(id);

        Assertions.assertTrue(result);
    }

    @Test
    public void deleteBookNotFound() {
        RuntimeException exception = Assertions.assertThrows(
                RuntimeException.class,
                () -> bookService.delete(null)
        );
        Assertions.assertEquals("Book not found", exception.getMessage());
    }

    @Test
    public void findAllBookByPersonId() {
        Integer personId = 1;
        List<Book> books = new ArrayList<>();
        books.add(new Book(
                1,
                "bookTitleTest1",
                "authorTest1",
                1803,
                null
        ));
        books.add(new Book(
                2,
                "bookTitleTest2",
                "authorTest2",
                2000,
                null
        ));

        Optional<List<Book>> optionalBooks = Optional.of(books);

        Mockito.doReturn(optionalBooks).when(mockBookRepository).findAllBookByPersonId(Mockito.anyInt());

        List<BookDtoOut> result = bookService.findAllBookByPersonId(personId);

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
