package ru.kobzeva.aston.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kobzeva.aston.dto.PersonDtoIn;
import ru.kobzeva.aston.dto.PersonDtoUpdate;
import ru.kobzeva.aston.service.classes.PersonServiceImpl;
import ru.kobzeva.aston.service.interfaces.PersonService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Field;

@ExtendWith(MockitoExtension.class)
public class PersonServletTest {
    private static PersonService mockPersonService;
    @InjectMocks
    private static PersonServlet personServlet;
    private static PersonServiceImpl realPersonService;
    @Mock
    private HttpServletRequest mockRequest;
    @Mock
    private HttpServletResponse mockResponse;
    @Mock
    private BufferedReader mockBufferedReader;

    private static void setMock(PersonService mock) {
        try {
            Field personService = PersonServiceImpl.class.getDeclaredField("personService");
            personService.setAccessible(true);
            realPersonService = (PersonServiceImpl) personService.get(personService);
            personService.set(personService, mock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    static void init() {
        mockPersonService = Mockito.mock(PersonService.class);
        setMock(mockPersonService);
        personServlet = new PersonServlet();
    }

    @AfterAll
    static void afterAll() throws Exception {
        Field personService = PersonServiceImpl.class.getDeclaredField("personService");
        personService.setAccessible(true);
        personService.set(personService, realPersonService);
    }

    @BeforeEach
    public void mockInit() throws IOException {
        Mockito.doReturn(new PrintWriter(Writer.nullWriter())).when(mockResponse).getWriter();
    }

    @AfterEach
    public void mockReset() {
        Mockito.reset(mockPersonService);
    }

    @Test
    public void doPost() throws IOException {
        String firstName = "new firstName";
        String lastName = "new lastName";
        Integer birthYear = 1970;

        String json = "{" +
                "\"firstName\" : \"" + firstName + "\"," +
                "\"lastName\" :\"" + lastName + "\"," +
                "\"birthYear\" :" + birthYear +
                "}";

        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn(json, null).when(mockBufferedReader).readLine();
        personServlet.doPost(mockRequest, mockResponse);

        ArgumentCaptor<PersonDtoIn> argumentCaptor = ArgumentCaptor.forClass(PersonDtoIn.class);
        Mockito.verify(mockPersonService).create(argumentCaptor.capture());

        PersonDtoIn result = argumentCaptor.getValue();
        Assertions.assertEquals(firstName, result.getFirstName());
        Assertions.assertEquals(lastName, result.getLastName());
        Assertions.assertEquals(birthYear, result.getBirthYear());
    }

    @Test
    public void doPostBadRequest() throws IOException {
        String firstName = "new firstName";

        String json = "{\"first_Name\" : \"" + firstName + "\"}";

        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn(json, (Object) null).when(mockBufferedReader).readLine();

        personServlet.doPost(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    public void doGet() throws IOException {
        Mockito.doReturn("person/1").when(mockRequest).getPathInfo();

        personServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockPersonService).read(Mockito.anyInt());
    }

    @Test
    void doGetPersonNotFound() throws IOException, RuntimeException {
        Mockito.doReturn("person/100").when(mockRequest).getPathInfo();
        Mockito.doThrow(new RuntimeException("Person not found")).when(mockPersonService).read(100);

        personServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void doGetBadRequest() throws Exception {
        Mockito.doReturn("person/2b").when(mockRequest).getPathInfo();

        personServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    public void doPut() throws IOException {
        Integer id = 1;
        String firstName = "new firstName";
        String lastName = "new lastName";
        Integer birthYear = 1970;

        String json = "{" +
                "\"id\" : \"" + id + "\"," +
                "\"firstName\" : \"" + firstName + "\"," +
                "\"lastName\" :\"" + lastName + "\"," +
                "\"birthYear\" :" + birthYear +
                "}";

        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn(json, (Object) null).when(mockBufferedReader).readLine();

        personServlet.doPut(mockRequest, mockResponse);

        ArgumentCaptor<PersonDtoUpdate> argumentCaptor = ArgumentCaptor.forClass(PersonDtoUpdate.class);
        Mockito.verify(mockPersonService).update(argumentCaptor.capture());

        PersonDtoUpdate result = argumentCaptor.getValue();
        Assertions.assertEquals(firstName, result.getFirstName());
        Assertions.assertEquals(lastName, result.getLastName());
        Assertions.assertEquals(birthYear, result.getBirthYear());
    }

    @Test
    public void doPutBadRequest() throws IOException {
        String firstName = "new firstName";

        String json = "{\"first_Name\" : \"" + firstName + "\"}";

        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn(json, (Object) null).when(mockBufferedReader).readLine();

        personServlet.doPut(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void doDelete() throws IOException {
        Mockito.doReturn("person/2").when(mockRequest).getPathInfo();

        personServlet.doDelete(mockRequest, mockResponse);

        Mockito.verify(mockPersonService).delete(Mockito.anyInt());
        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Test
    void doDeleteNotFoundPerson() throws IOException {
        Mockito.doReturn("person/100").when(mockRequest).getPathInfo();
        Mockito.doThrow(new RuntimeException("Person not found")).when(mockPersonService).delete(100);

        personServlet.doDelete(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_NOT_FOUND);
        Mockito.verify(mockPersonService).delete(100);
    }

    @Test
    void doDeleteBadRequest() throws IOException {
        Mockito.doReturn("person/a100").when(mockRequest).getPathInfo();

        personServlet.doDelete(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}
