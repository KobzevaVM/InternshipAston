package ru.kobzeva.aston.repository.interfaces;

import ru.kobzeva.aston.entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends Repository<Book, Integer> {
    Book create(Book b);

    Optional<Book> read(Integer id);

    boolean update(Book b);

    boolean delete(Integer id);

    Optional<List<Book>> findAllBookByPersonId(Integer id);
}
