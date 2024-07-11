package ru.kobzeva.aston.repository.interfaces;

import java.util.Optional;

public interface Repository<T, E> {
    T create(T t);

    Optional<T> read(E e);

    boolean update(T t);

    boolean delete(E e);
}
