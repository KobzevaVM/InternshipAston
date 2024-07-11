package ru.kobzeva.aston.repository.interfaces;

import ru.kobzeva.aston.entity.Person;

import java.util.Optional;

public interface PersonRepository extends Repository<Person, Integer> {
    Person create(Person p);

    Optional<Person> read(Integer id);

    boolean update(Person p);

    boolean delete(Integer id);

}
