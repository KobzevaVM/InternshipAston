package ru.kobzeva.aston.repository.classes;

import org.junit.jupiter.api.*;
import ru.kobzeva.aston.entity.Person;
import ru.kobzeva.aston.repository.interfaces.PersonRepository;

import java.util.Optional;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonRepositoryImplTest {
    public static PersonRepository personRepository = PersonRepositoryImpl.getPersonRepository();
    public static Integer newId;

    @Test
    @Order(1)
    public void create() {
        String firstName = "new firstName";
        String lastName = "new lastYear";
        Integer birthYear = 2000;

        Person person = new Person(
                null,
                firstName,
                lastName,
                birthYear,
                null);
        person = personRepository.create(person);
        Optional<Person> result = personRepository.read(person.getId());
        result.ifPresent(value -> newId = value.getId());

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(firstName, result.get().getFirstName());
        Assertions.assertEquals(lastName, result.get().getLastName());
        Assertions.assertEquals(birthYear, result.get().getBirthYear());
    }

    @Test
    @Order(2)
    public void update() {
        String firstName = "update firstName";
        String lastName = "update lastYear";
        Integer birthYear = 2010;

        Person person = personRepository.read(newId).get();

        Assertions.assertNotEquals(firstName, person.getFirstName());
        Assertions.assertNotEquals(lastName, person.getLastName());
        Assertions.assertNotEquals(birthYear, person.getBirthYear());

        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setBirthYear(birthYear);

        personRepository.update(person);

        Person result = personRepository.read(newId).get();

        Assertions.assertEquals(firstName, result.getFirstName());
        Assertions.assertEquals(lastName, result.getLastName());
        Assertions.assertEquals(birthYear, result.getBirthYear());
    }

    @Test
    @Order(3)
    public void read() {
        String firstName = "update firstName";
        String lastName = "update lastYear";
        Integer birthYear = 2010;

        Person result = personRepository.read(newId).get();

        Assertions.assertEquals(firstName, result.getFirstName());
        Assertions.assertEquals(lastName, result.getLastName());
        Assertions.assertEquals(birthYear, result.getBirthYear());
    }

    @Test
    @Order(5)
    public void delete() {

        boolean result = personRepository.delete(newId);

        Assertions.assertEquals(true, result);
    }
}
