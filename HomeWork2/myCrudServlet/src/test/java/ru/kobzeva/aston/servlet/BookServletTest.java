package ru.kobzeva.aston.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kobzeva.aston.dto.BookDtoIn;
import ru.kobzeva.aston.dto.BookDtoUpdate;
import ru.kobzeva.aston.service.classes.BookServiceImpl;
import ru.kobzeva.aston.service.interfaces.BookService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Field;

@ExtendWith(MockitoExtension.class)
public class BookServletTest {
    private static BookService mockBookService;
    @InjectMocks
    private static BookServlet bookServlet;
    private static BookServiceImpl realBookService;
    @Mock
    private HttpServletRequest mockRequest;
    @Mock
    private HttpServletResponse mockResponse;
    @Mock
    private BufferedReader mockBufferedReader;

    private static void setMock(BookService mock) {
        try {
            Field bookService = BookServiceImpl.class.getDeclaredField("bookService");
            bookService.setAccessible(true);
            realBookService = (BookServiceImpl) bookService.get(bookService);
            bookService.set(bookService, mock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    static void init() {
        mockBookService = Mockito.mock(BookService.class);
        setMock(mockBookService);
        bookServlet = new BookServlet();
    }

    @AfterAll
    static void afterAll() throws Exception {
        Field bookService = BookServiceImpl.class.getDeclaredField("bookService");
        bookService.setAccessible(true);
        bookService.set(bookService, realBookService);
    }

    @BeforeEach
    public void mockInit() throws IOException {
        Mockito.doReturn(new PrintWriter(Writer.nullWriter())).when(mockResponse).getWriter();
    }

    @AfterEach
    public void mockReset() {
        Mockito.reset(mockBookService);
    }

    @Test
    public void doPost() throws IOException {
        String bookTitle = "new bookTitle";
        String author = "new author";
        Integer year = 1950;

        String json = "{" +
                "\"bookTitle\" : \"" + bookTitle + "\"," +
                "\"author\" :\"" + author + "\"," +
                "\"year\" :" + year +
                "}";

        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn(json, null).when(mockBufferedReader).readLine();
        bookServlet.doPost(mockRequest, mockResponse);

        ArgumentCaptor<BookDtoIn> argumentCaptor = ArgumentCaptor.forClass(BookDtoIn.class);
        Mockito.verify(mockBookService).create(argumentCaptor.capture());

        BookDtoIn result = argumentCaptor.getValue();
        Assertions.assertEquals(bookTitle, result.getBookTitle());
        Assertions.assertEquals(author, result.getAuthor());
        Assertions.assertEquals(year, result.getYear());
    }

    @Test
    public void doPostBadRequest() throws IOException {
        String bookTitle = "new bookTitle";

        String json = "{\"book_Title\" : \"" + bookTitle + "\"}";

        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn(json, (Object) null).when(mockBufferedReader).readLine();

        bookServlet.doPost(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    public void doGet() throws IOException {
        Mockito.doReturn("book/1").when(mockRequest).getPathInfo();

        bookServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockBookService).read(Mockito.anyInt());
    }

    @Test
    void doGetBookNotFound() throws IOException, RuntimeException {
        Mockito.doReturn("book/100").when(mockRequest).getPathInfo();
        Mockito.doThrow(new RuntimeException("Book not found")).when(mockBookService).read(100);

        bookServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void doGetBadRequest() throws Exception {
        Mockito.doReturn("book/2b").when(mockRequest).getPathInfo();

        bookServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    public void doPut() throws IOException {
        Integer id = 1;
        String bookTitle = "new bookTitle";
        String author = "new author";
        Integer year = 1950;

        String json = "{" +
                "\"id\" : \"" + id + "\"," +
                "\"bookTitle\" : \"" + bookTitle + "\"," +
                "\"author\" :\"" + author + "\"," +
                "\"year\" :" + year +
                "}";

        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn(json, (Object) null).when(mockBufferedReader).readLine();

        bookServlet.doPut(mockRequest, mockResponse);

        ArgumentCaptor<BookDtoUpdate> argumentCaptor = ArgumentCaptor.forClass(BookDtoUpdate.class);
        Mockito.verify(mockBookService).update(argumentCaptor.capture());

        BookDtoUpdate result = argumentCaptor.getValue();
        Assertions.assertEquals(bookTitle, result.getBookTitle());
        Assertions.assertEquals(author, result.getAuthor());
        Assertions.assertEquals(year, result.getYear());
    }

    @Test
    public void doPutBadRequest() throws IOException {
        String bookTitle = "new bookTitle";

        String json = "{\"book_Title\" : \"" + bookTitle + "\"}";

        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn(json, (Object) null).when(mockBufferedReader).readLine();

        bookServlet.doPut(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void doDelete() throws IOException {
        Mockito.doReturn("book/2").when(mockRequest).getPathInfo();

        bookServlet.doDelete(mockRequest, mockResponse);

        Mockito.verify(mockBookService).delete(Mockito.anyInt());
        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Test
    void doDeleteNotFoundBook() throws IOException {
        Mockito.doReturn("book/100").when(mockRequest).getPathInfo();
        Mockito.doThrow(new RuntimeException("Book not found")).when(mockBookService).delete(100);

        bookServlet.doDelete(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_NOT_FOUND);
        Mockito.verify(mockBookService).delete(100);
    }

    @Test
    void doDeleteBadRequest() throws IOException {
        Mockito.doReturn("book/a100").when(mockRequest).getPathInfo();

        bookServlet.doDelete(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

}
