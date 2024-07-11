package ru.kobzeva.aston.repository.classes;

import ru.kobzeva.aston.datasource.ConnectionManager;
import ru.kobzeva.aston.entity.Book;
import ru.kobzeva.aston.repository.interfaces.BookRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepositoryImpl implements BookRepository {
    private static final String CREATE = "insert into book (book_title, author, year, person_id) values(?, ?, ?, ?)";
    private static final String READ = "select * from book where id = ?";
    private static final String UPDATE = "update book set book_title = ?, author = ?, year = ?, person_id = ? where id = ?";
    private static final String DELETE = "delete from book where id = ?";
    private static final String FIND_ALL_BOOKS = "select * from book where person_id = ?";
    private static final ConnectionManager connectionManager = ConnectionManager.getConnectionManager();
    private static BookRepository bookRepository;

    private BookRepositoryImpl() {
    }

    public static synchronized BookRepository getBookRepository() {
        if (bookRepository == null) {
            bookRepository = new BookRepositoryImpl();
        }
        return bookRepository;
    }

    @Override
    public Book create(Book b) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, b.getBookTitle());
            preparedStatement.setString(2, b.getAuthor());
            preparedStatement.setInt(3, b.getYear());
            if (b.getPersonId() == null) {
                preparedStatement.setNull(4, Types.INTEGER);
            } else {
                preparedStatement.setInt(4, b.getPersonId());
            }

            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                b = new Book(
                        resultSet.getInt("id"),
                        b.getBookTitle(),
                        b.getAuthor(),
                        b.getYear(),
                        b.getPersonId()
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("Book not created");
        }

        return b;
    }

    @Override
    public Optional<Book> read(Integer id) {
        Book book = null;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(READ)) {

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                book = createBook(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Book not found");
        }
        return Optional.ofNullable(book);
    }

    @Override
    public boolean update(Book b) {
        boolean isUpdated;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
            preparedStatement.setString(1, b.getBookTitle());
            preparedStatement.setString(2, b.getAuthor());
            preparedStatement.setInt(3, b.getYear());
            System.out.println(b.getPersonId());
            System.out.println();
            if (b.getPersonId() == null) {
                preparedStatement.setObject(4, null);
            } else {
                preparedStatement.setInt(4, b.getPersonId());
            }
            preparedStatement.setInt(5, b.getId());
            isUpdated = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Book not updated");
        }

        return isUpdated;
    }

    @Override
    public boolean delete(Integer id) {
        boolean isDelete;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE);) {

            preparedStatement.setInt(1, id);
            isDelete = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Book not deleted");
        }
        return isDelete;
    }

    public Optional<List<Book>> findAllBookByPersonId(Integer id) {
        List<Book> books = new ArrayList<>();

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_BOOKS)) {

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                books.add(createBook(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Book not found");
        }
        return Optional.of(books);
    }

    private Book createBook(ResultSet resultSet) {
        Book book;
        try {
            book = new Book(
                    resultSet.getInt("id"),
                    resultSet.getString("book_title"),
                    resultSet.getString("author"),
                    resultSet.getInt("year"),
                    (Integer) resultSet.getObject("person_id")
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return book;
    }
}
