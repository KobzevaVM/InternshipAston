package ru.kobzeva.aston.service.interfaces;

import ru.kobzeva.aston.dto.PersonDtoIn;
import ru.kobzeva.aston.dto.PersonDtoOut;
import ru.kobzeva.aston.dto.PersonDtoUpdate;

public interface PersonService {
    PersonDtoOut create(PersonDtoIn personDto);

    PersonDtoOut read(Integer personId);

    boolean update(PersonDtoUpdate personDto);

    boolean delete(Integer personId);
}
