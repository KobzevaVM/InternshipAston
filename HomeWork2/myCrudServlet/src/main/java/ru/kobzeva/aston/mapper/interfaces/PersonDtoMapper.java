package ru.kobzeva.aston.mapper.interfaces;

import ru.kobzeva.aston.dto.PersonDtoIn;
import ru.kobzeva.aston.dto.PersonDtoOut;
import ru.kobzeva.aston.dto.PersonDtoUpdate;
import ru.kobzeva.aston.entity.Person;

public interface PersonDtoMapper {
    Person map(PersonDtoIn personDtoIn);

    Person map(PersonDtoUpdate personDtoUpdate);

    PersonDtoOut map(Person person);
}
