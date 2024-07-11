package ru.kobzeva.aston.service.classes;

import ru.kobzeva.aston.dto.PersonDtoIn;
import ru.kobzeva.aston.dto.PersonDtoOut;
import ru.kobzeva.aston.dto.PersonDtoUpdate;
import ru.kobzeva.aston.entity.Person;
import ru.kobzeva.aston.mapper.classes.PersonDtoMapperImpl;
import ru.kobzeva.aston.mapper.interfaces.PersonDtoMapper;
import ru.kobzeva.aston.repository.classes.PersonRepositoryImpl;
import ru.kobzeva.aston.repository.interfaces.PersonRepository;
import ru.kobzeva.aston.service.interfaces.PersonService;

public class PersonServiceImpl implements PersonService {
    private static final PersonDtoMapper personDtoMapper = PersonDtoMapperImpl.getPersonDtoMapper();
    private static PersonService personService;
    private final PersonRepository personRepository = PersonRepositoryImpl.getPersonRepository();

    private PersonServiceImpl() {
    }

    public static synchronized PersonService getPersonService() {
        if (personService == null) {
            personService = new PersonServiceImpl();
        }
        return personService;
    }

    private void checkPerson(Integer personId) throws RuntimeException {
        if (personRepository.read(personId).isEmpty()) {
            throw new RuntimeException("Person not found");
        }
    }

    @Override
    public PersonDtoOut create(PersonDtoIn personDtoIn) {
        Person person = personDtoMapper.map(personDtoIn);
        person = personRepository.create(person);
        return personDtoMapper.map(person);
    }

    @Override
    public PersonDtoOut read(Integer personId) {
        Person person = personRepository.read(personId).orElseThrow(() -> new RuntimeException("Person not found"));
        return personDtoMapper.map(person);
    }

    @Override
    public boolean update(PersonDtoUpdate personDtoUpdate) {
        checkPerson(personDtoUpdate.getId());
        Person person = personDtoMapper.map(personDtoUpdate);
        return personRepository.update(person);
    }

    @Override
    public boolean delete(Integer personId) {
        checkPerson(personId);
        return personRepository.delete(personId);
    }
}
