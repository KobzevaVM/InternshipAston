package ru.kobzeva.aston.mapper.classes;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.kobzeva.aston.dto.PersonDtoIn;
import ru.kobzeva.aston.dto.PersonDtoOut;
import ru.kobzeva.aston.dto.PersonDtoUpdate;
import ru.kobzeva.aston.entity.Book;
import ru.kobzeva.aston.entity.Person;
import ru.kobzeva.aston.mapper.interfaces.PersonDtoMapper;

import java.util.ArrayList;
import java.util.List;

public class PersonDtoMapperImplTest {
    private PersonDtoMapper personDtoMapper;

    @BeforeEach
    void setPersonDtoMapper() {
        personDtoMapper = PersonDtoMapperImpl.getPersonDtoMapper();
    }

    @DisplayName(value = "Person map(PersonDtoIn)")
    @Test
    public void mapDtoIn() {
        PersonDtoIn personDtoIn = new PersonDtoIn(
                "firstNameTest",
                "lastNameTest",
                1994
        );
        Person result = personDtoMapper.map(personDtoIn);

        Assertions.assertNull(result.getId());
        System.out.println(result.getId());
        Assertions.assertEquals(personDtoIn.getFirstName(), result.getFirstName());
        Assertions.assertEquals(personDtoIn.getLastName(), result.getLastName());
        Assertions.assertEquals(personDtoIn.getBirthYear(), result.getBirthYear());
    }

    @DisplayName(value = "Person map(PersonDtoUpdate)")
    @Test
    public void mapDtoUpdate() {
        PersonDtoUpdate personDtoUpdate = new PersonDtoUpdate(
                1,
                "firstNameTest",
                "lastNameTest",
                1901
        );
        Person result = personDtoMapper.map(personDtoUpdate);

        Assertions.assertEquals(personDtoUpdate.getId(), result.getId());
        Assertions.assertEquals(personDtoUpdate.getFirstName(), result.getFirstName());
        Assertions.assertEquals(personDtoUpdate.getLastName(), result.getLastName());
        Assertions.assertEquals(personDtoUpdate.getBirthYear(), result.getBirthYear());
    }

    @DisplayName(value = "PersonDtoOut map(Person person)")
    @Test
    public void mapDtoOut() {
        List<Book> books = new ArrayList<>();
        books.add(new Book(100,
                "Harry Potter",
                "J. K. Rowling",
                1997,
                3));
        books.add(new Book(101,
                "Harry Potter 2",
                "J. K. Rowling",
                1998,
                3));
        Person person = new Person(
                3,
                "firstNameTest",
                "lastNameTest",
                1901,
                books
        );
        PersonDtoOut result = personDtoMapper.map(person);

        Assertions.assertEquals(person.getId(), result.getId());
        Assertions.assertEquals(person.getFirstName(), result.getFirstName());
        Assertions.assertEquals(person.getLastName(), result.getLastName());
        Assertions.assertEquals(person.getBirthYear(), result.getBirthYear());
        Assertions.assertEquals(person.getBooks().size(), result.getBooks().size());
    }
}
