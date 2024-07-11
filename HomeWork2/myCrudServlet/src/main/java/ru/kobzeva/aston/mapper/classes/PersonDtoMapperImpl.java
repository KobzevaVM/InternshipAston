package ru.kobzeva.aston.mapper.classes;

import ru.kobzeva.aston.dto.PersonDtoIn;
import ru.kobzeva.aston.dto.PersonDtoOut;
import ru.kobzeva.aston.dto.PersonDtoUpdate;
import ru.kobzeva.aston.entity.Person;
import ru.kobzeva.aston.mapper.interfaces.BookDtoMapper;
import ru.kobzeva.aston.mapper.interfaces.PersonDtoMapper;

public class PersonDtoMapperImpl implements PersonDtoMapper {
    private static final BookDtoMapper bookDtoMapper = BookDtoMapperImpl.getBookDtoMapper();
    private static PersonDtoMapper personDtoMapper;

    private PersonDtoMapperImpl() {
    }

    public static synchronized PersonDtoMapper getPersonDtoMapper() {
        if (personDtoMapper == null) {
            personDtoMapper = new PersonDtoMapperImpl();
        }
        return personDtoMapper;
    }

    @Override
    public Person map(PersonDtoIn personDtoIn) {
        return new Person(
                null,
                personDtoIn.getFirstName(),
                personDtoIn.getLastName(),
                personDtoIn.getBirthYear(),
                null
        );
    }

    @Override
    public Person map(PersonDtoUpdate personDtoUpdate) {
        return new Person(
                personDtoUpdate.getId(),
                personDtoUpdate.getFirstName(),
                personDtoUpdate.getLastName(),
                personDtoUpdate.getBirthYear(),
                null
        );
    }

    @Override
    public PersonDtoOut map(Person person) {
        return new PersonDtoOut(
                person.getId(),
                person.getFirstName(),
                person.getLastName(),
                person.getBirthYear(),
                bookDtoMapper.map(person.getBooks())
        );
    }
}
