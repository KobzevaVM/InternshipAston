package ru.kobzeva.aston.repository.classes;

import lombok.NoArgsConstructor;
import ru.kobzeva.aston.datasource.ConnectionManager;
import ru.kobzeva.aston.entity.Book;
import ru.kobzeva.aston.entity.Person;
import ru.kobzeva.aston.repository.interfaces.BookRepository;
import ru.kobzeva.aston.repository.interfaces.PersonRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PersonRepositoryImpl implements PersonRepository {
    private static final String CREATE = "insert into person (first_name, last_name, birth_year) values(?, ?, ?)";
    private static final String READ = "select * from person where id = ?";
    private static final String UPDATE = "update person set first_name = ?, last_name = ?, birth_year = ? where id = ?";
    private static final String DELETE = "delete from person where id = ?";

    private static PersonRepository personRepository;
    private final BookRepository bookRepository = BookRepositoryImpl.getBookRepository();

    private final ConnectionManager connectionManager = ConnectionManager.getConnectionManager();

    private PersonRepositoryImpl() {
    }

    public static synchronized PersonRepository getPersonRepository() {
        if (personRepository == null) {
            personRepository = new PersonRepositoryImpl();
        }
        return personRepository;
    }

    @Override
    public Person create(Person p) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, p.getFirstName());
            preparedStatement.setString(2, p.getLastName());
            preparedStatement.setInt(3, p.getBirthYear());
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                p = new Person(
                        resultSet.getInt("id"),
                        p.getFirstName(),
                        p.getLastName(),
                        p.getBirthYear(),
                        p.getBooks()
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("Person not created");
        }

        return p;
    }

    @Override
    public Optional<Person> read(Integer id) {
        Person person = null;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(READ)) {

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                person = createPerson(resultSet, id);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Person not found");
        }
        return Optional.ofNullable(person);
    }

    @Override
    public boolean update(Person p) {
        boolean isUpdate;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
            preparedStatement.setString(1, p.getFirstName());
            preparedStatement.setString(2, p.getLastName());
            preparedStatement.setInt(3, p.getBirthYear());
            preparedStatement.setInt(4, p.getId());
            isUpdate = preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Person not updated");
        }

        return isUpdate;
    }

    @Override
    public boolean delete(Integer id) {
        boolean isDelete;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE);) {

            preparedStatement.setInt(1, id);
            isDelete = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Person not deleted");
        }
        return isDelete;
    }

    private Person createPerson(ResultSet resultSet, Integer id) {
        Person person;
        List<Book> b = null;
        Optional<List<Book>> books = bookRepository.findAllBookByPersonId(id);

        if (books.isPresent()) {
            b = new ArrayList<>(books.get());
        }

        try {
            person = new Person(
                    resultSet.getInt("id"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    resultSet.getInt("birth_year"),
                    b
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return person;
    }
}
