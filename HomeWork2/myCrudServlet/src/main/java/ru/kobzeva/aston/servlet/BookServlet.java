package ru.kobzeva.aston.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.kobzeva.aston.dto.BookDtoIn;
import ru.kobzeva.aston.dto.BookDtoOut;
import ru.kobzeva.aston.dto.BookDtoUpdate;
import ru.kobzeva.aston.service.classes.BookServiceImpl;
import ru.kobzeva.aston.service.interfaces.BookService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@WebServlet(urlPatterns = {"/book/*"})
public class BookServlet extends HttpServlet {
    private final BookService bookService = BookServiceImpl.getBookService();
    private final ObjectMapper objectMapper;

    public BookServlet() {
        this.objectMapper = new ObjectMapper();
    }

    private static void setConType(HttpServletResponse resp) {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
    }

    private static String convertFromJson(HttpServletRequest req) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = req.getReader();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setConType(resp);
        String json = convertFromJson(req);

        String answer = null;
        Optional<BookDtoIn> bookResp;
        try {
            bookResp = Optional.ofNullable(objectMapper.readValue(json, BookDtoIn.class));
            BookDtoIn book = bookResp.orElseThrow(IllegalArgumentException::new);
            answer = objectMapper.writeValueAsString(bookService.create(book));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            answer = "Incorrect book data";
        }
        PrintWriter printWriter = resp.getWriter();
        printWriter.write(answer);
        printWriter.flush();
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setConType(resp);

        String answer = "";
        try {
            String[] pathPart = req.getPathInfo().split("/");
            Integer bookId = Integer.parseInt(pathPart[1]);
            BookDtoOut bookDtoOut = bookService.read(bookId);
            resp.setStatus(HttpServletResponse.SC_OK);
            answer = objectMapper.writeValueAsString(bookDtoOut);
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Book not found")) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                answer = "Book not found";
            } else {
                answer = "Bad request";
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            answer = "Bad request";
        }
        PrintWriter printWriter = resp.getWriter();
        printWriter.write(answer);
        printWriter.flush();
    }

    @Override
    public void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setConType(resp);
        String json = convertFromJson(req);

        String answer = "";
        Optional<BookDtoUpdate> bookResp;
        try {
            bookResp = Optional.ofNullable(objectMapper.readValue(json, BookDtoUpdate.class));
            BookDtoUpdate bookDtoUpdate = bookResp.orElseThrow(IllegalArgumentException::new);
            bookService.update(bookDtoUpdate);
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Book not found")) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                answer = "Book not found";
            } else {
                answer = "Incorrect book data";
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            answer = "Incorrect book data";
        }
        PrintWriter printWriter = resp.getWriter();
        printWriter.write(answer);
        printWriter.flush();
    }

    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setConType(resp);
        String answer = "";
        try {
            String[] pathPart = req.getPathInfo().split("/");
            Integer bookId = Integer.parseInt(pathPart[1]);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            bookService.delete(bookId);
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Book not found")) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                answer = "Book not found";
            } else {
                answer = "Bad request";
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            answer = "Bad request.";
        }
        PrintWriter printWriter = resp.getWriter();
        printWriter.write(answer);
        printWriter.flush();
    }
}
