package ru.kobzeva.aston.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.kobzeva.aston.dto.PersonDtoIn;
import ru.kobzeva.aston.dto.PersonDtoOut;
import ru.kobzeva.aston.dto.PersonDtoUpdate;
import ru.kobzeva.aston.service.classes.PersonServiceImpl;
import ru.kobzeva.aston.service.interfaces.PersonService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@WebServlet(urlPatterns = {"/person/*"})
public class PersonServlet extends HttpServlet {
    private final PersonService personService = PersonServiceImpl.getPersonService();
    private final ObjectMapper objectMapper;

    public PersonServlet() {
        this.objectMapper = new ObjectMapper();
    }

    private static void setConType(HttpServletResponse resp) {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
    }

    private static String convertFromJson(HttpServletRequest req) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = req.getReader();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setConType(resp);
        String json = convertFromJson(req);

        String answer = null;
        Optional<PersonDtoIn> personResp;
        try {
            personResp = Optional.ofNullable(objectMapper.readValue(json, PersonDtoIn.class));
            PersonDtoIn person = personResp.orElseThrow(IllegalArgumentException::new);
            answer = objectMapper.writeValueAsString(personService.create(person));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            answer = "Incorrect person data";
        }
        PrintWriter printWriter = resp.getWriter();
        printWriter.write(answer);
        printWriter.flush();
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setConType(resp);

        String answer = "";
        try {
            String[] pathPart = req.getPathInfo().split("/");
            Integer personId = Integer.parseInt(pathPart[1]);
            PersonDtoOut personDtoOut = personService.read(personId);
            resp.setStatus(HttpServletResponse.SC_OK);
            answer = objectMapper.writeValueAsString(personDtoOut);
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Person not found")) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                answer = "Person not found";
            } else {
                answer = "Bad request";
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            answer = "Bad request";
        }
        PrintWriter printWriter = resp.getWriter();
        printWriter.write(answer);
        printWriter.flush();
    }

    @Override
    public void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setConType(resp);
        String json = convertFromJson(req);

        String answer = "";
        Optional<PersonDtoUpdate> userResp;
        try {
            userResp = Optional.ofNullable(objectMapper.readValue(json, PersonDtoUpdate.class));
            PersonDtoUpdate userUpdateDto = userResp.orElseThrow(IllegalArgumentException::new);
            personService.update(userUpdateDto);
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Person not found")) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                answer = "Person not found";
            } else {
                answer = "Incorrect book data";
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            answer = "Incorrect person data";
        }
        PrintWriter printWriter = resp.getWriter();
        printWriter.write(answer);
        printWriter.flush();
    }

    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setConType(resp);
        String answer = "";
        try {
            String[] pathPart = req.getPathInfo().split("/");
            Integer personId = Integer.parseInt(pathPart[1]);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            personService.delete(personId);
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Person not found")) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                answer = "Person not found";
            } else {
                answer = "Bad request";
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            answer = "Bad request.";
        }
        PrintWriter printWriter = resp.getWriter();
        printWriter.write(answer);
        printWriter.flush();
    }

}
