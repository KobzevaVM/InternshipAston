package ru.kobzeva.aston.service.classes;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import ru.kobzeva.aston.dto.PersonDtoIn;
import ru.kobzeva.aston.dto.PersonDtoOut;
import ru.kobzeva.aston.dto.PersonDtoUpdate;
import ru.kobzeva.aston.entity.Book;
import ru.kobzeva.aston.entity.Person;
import ru.kobzeva.aston.repository.classes.PersonRepositoryImpl;
import ru.kobzeva.aston.repository.interfaces.PersonRepository;
import ru.kobzeva.aston.service.interfaces.PersonService;

import java.lang.reflect.Field;
import java.util.Optional;

public class PersonServiceImplTest {
    private static PersonService personService;
    private static PersonRepository mockPersonRepository;
    private static PersonRepositoryImpl realPersonRepository;

    private static void setMock(PersonRepository mock) {
        try {
            // Получили доступ к нашему private репозиторию
            Field personRepository = PersonRepositoryImpl.class.getDeclaredField("personRepository");
            //Даем доступ для работы с нашим private репозиторием
            personRepository.setAccessible(true);
            //Получаем значение нашего private репозитория, чтобы вернуть в конце теста все обратно
            realPersonRepository = (PersonRepositoryImpl) personRepository.get(personRepository);
            //кладем вместо нашего реального репозитория Mock
            personRepository.set(personRepository, mock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //Тестируем сервис, поэтому в сервисе надо заменить реальный репозиторий на Mock,
    //чтобы при вызове методов не писал к нам в БД
    @BeforeAll
    static void replaceRepository() {
        mockPersonRepository = Mockito.mock(PersonRepository.class);
        setMock(mockPersonRepository);
        personService = PersonServiceImpl.getPersonService();
    }

    // После тестирования возвращаем в наше private поле ссылку на реальный репозиторий
    @AfterAll
    static void revertRepository() throws Exception {
        Field personRepository = PersonRepositoryImpl.class.getDeclaredField("personRepository");
        personRepository.setAccessible(true);
        personRepository.set(personRepository, realPersonRepository);
    }

    @BeforeEach
    public void mockReset() {
        Mockito.reset(mockPersonRepository);
    }

    @Test
    public void create() {
        Integer id = 1;

        PersonDtoIn personDtoIn = new PersonDtoIn(
                "firstName",
                "lastName",
                1950
        );
        Person person = new Person(
                id,
                "firstName",
                "lastName",
                1950,
                null
        );

        //В методе create в Service вызывается метод create у Repository, который возвращает Person.
        //Сказали ему какого человека возвращать, т.к. это Mock.
        Mockito.doReturn(person).when(mockPersonRepository).create(Mockito.any(Person.class));

        PersonDtoOut result = personService.create(personDtoIn);

        Assertions.assertEquals(id, result.getId());
        Assertions.assertEquals(personDtoIn.getFirstName(), result.getFirstName());
        Assertions.assertEquals(personDtoIn.getLastName(), result.getLastName());
        Assertions.assertEquals(personDtoIn.getBirthYear(), result.getBirthYear());
        Assertions.assertEquals(0, result.getBooks().size());
    }

    @Test
    public void read() {
        Integer id = 1;

        Optional<Person> person = Optional.of(
                new Person(
                        id,
                        "firstName",
                        "lastName",
                        1950,
                        null
                )
        );

        //В методе create в Service вызывается метод create у Repository, который возвращает Optional<Person>.
        //Сказали ему какую книгу(Optional<Person>) возвращать, т.к. это Mock.
        Mockito.doReturn(person).when(mockPersonRepository).read(Mockito.anyInt());

        PersonDtoOut result = personService.read(1);

        Assertions.assertEquals(id, result.getId());
        Assertions.assertEquals(person.get().getFirstName(), result.getFirstName());
        Assertions.assertEquals(person.get().getLastName(), result.getLastName());
        Assertions.assertEquals(person.get().getBirthYear(), result.getBirthYear());
        Assertions.assertEquals(0, result.getBooks().size());
    }

    @Test
    public void readPersonNotFound() {
        Optional<Book> book = Optional.empty();

        Mockito.doReturn(book).when(mockPersonRepository).read(Mockito.anyInt());

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> personService.read(1));
        Assertions.assertEquals("Person not found", exception.getMessage());
    }

    @Test
    public void update() {
        Integer id = 1;

        PersonDtoUpdate personDtoUpdate = new PersonDtoUpdate(
                id,
                "firstName",
                "lastName",
                1950
        );

        Optional<Person> person = Optional.of(
                new Person(
                        id,
                        "firstName",
                        "lastName",
                        1950,
                        null
                )
        );

        Mockito.doReturn(true).when(mockPersonRepository).update(Mockito.any(Person.class));
        Mockito.doReturn(person).when(mockPersonRepository).read(Mockito.anyInt());

        boolean result = personService.update(personDtoUpdate);

        Assertions.assertTrue(result);
    }

    @Test
    public void updatePersonNotFound() {

        PersonDtoUpdate personDtoUpdate = new PersonDtoUpdate(
                null,
                "firstName",
                "lastName",
                1950
        );

        RuntimeException exception = Assertions.assertThrows(
                RuntimeException.class,
                () -> personService.update(personDtoUpdate)
        );
        Assertions.assertEquals("Person not found", exception.getMessage());
    }

    @Test
    public void delete() {
        Integer id = 1;

        Optional<Person> person = Optional.of(
                new Person(
                        id,
                        "firstName",
                        "lastName",
                        1950,
                        null
                )
        );

        Mockito.doReturn(true).when(mockPersonRepository).delete(Mockito.anyInt());
        Mockito.doReturn(person).when(mockPersonRepository).read(Mockito.anyInt());

        boolean result = personService.delete(id);

        Assertions.assertTrue(result);
    }

    @Test
    public void deletePersonNotFound() {
        RuntimeException exception = Assertions.assertThrows(
                RuntimeException.class,
                () -> personService.delete(null)
        );
        Assertions.assertEquals("Person not found", exception.getMessage());
    }
}
