package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.filmorate.controllers.ExceptionApiHandler;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.models.Film;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class FilmControllerTests {

    private static ObjectMapper mapper;
    private MockMvc mockMvc;
    private Film film;

    @InjectMocks
    private FilmController filmController;

    @BeforeAll
    public static void prepare() {
        mapper = new ObjectMapper();
    }

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(filmController).setControllerAdvice(new ExceptionApiHandler()).build();
        film = new Film("Test test", "Description", LocalDate.parse("1945-05-09"), 100);
    }

    @Test
    public void testPostFilmCorrectDataSuccess() throws Exception {
        String json = mapper.writeValueAsString(film);

        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andExpect(status().isOk())
                .andExpect(jsonPath("name", Matchers.equalTo("Test test")));
    }

    @Test
    public void testPostFilmWithDuplicatedNameSuccess() throws Exception {
        Film film2 = new Film("Test test", "Description", LocalDate.parse("1945-05-10"), 100);
        String json = mapper.writeValueAsString(film);
        String json2 = mapper.writeValueAsString(film2);

        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andExpect(status().isOk())
                .andExpect(jsonPath("name", Matchers.equalTo("Test test")));
        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON)
                        .content(json2)).andExpect(status().isOk())
                .andExpect(jsonPath("name", Matchers.equalTo("Test test")));
    }

    @Test
    public void testPostFilmWithDuplicatedReleaseDateSuccess() throws Exception {
        Film film2 = new Film("Test test2", "Description", LocalDate.parse("1945-05-09"), 100);
        String json = mapper.writeValueAsString(film);
        String json2 = mapper.writeValueAsString(film2);

        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andExpect(status().isOk())
                .andExpect(jsonPath("name", Matchers.equalTo("Test test")));
        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON)
                        .content(json2)).andExpect(status().isOk())
                .andExpect(jsonPath("name", Matchers.equalTo("Test test2")));
    }

    @Test
    public void testPostFilmWithDuplicatedReleaseDateAndNameBadRequest() throws Exception {
        Film film2 = new Film("Test test", "Description", LocalDate.parse("1945-05-09"), 100);
        String json = mapper.writeValueAsString(film);
        String json2 = mapper.writeValueAsString(film2);

        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andExpect(status().isOk())
                .andExpect(jsonPath("name", Matchers.equalTo("Test test")));
        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON)
                        .content(json2)).andExpect(status().isConflict())
                .andExpect(jsonPath("message", Matchers.equalTo("Film is already exist in the DB.")));
    }

    @Test
    public void testPostFilmWith201CharsDescriptionBadRequest() throws Exception {
        film.setDescription(RandomStringUtils.randomAlphabetic(201));
        String json = mapper.writeValueAsString(film);

        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andExpect(status().isBadRequest())
                .andExpect(jsonPath("message", Matchers.equalTo("Max. length for the Description value is limited by 200 chars")));
    }

    @Test
    public void testPostFilmWith200CharsDescriptionSuccess() throws Exception {
        film.setDescription(RandomStringUtils.randomAlphabetic(200));
        String json = mapper.writeValueAsString(film);

        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andExpect(status().isOk())
                .andExpect(jsonPath("name", Matchers.equalTo("Test test")));
    }

    @Test
    public void testPostFilmWithCorrectReleaseDateSuccess() throws Exception {
        film.setReleaseDate(LocalDate.parse("1895-12-28"));
        String json = mapper.writeValueAsString(film);

        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andExpect(status().isOk())
                .andExpect(jsonPath("name", Matchers.equalTo("Test test")));
    }

    @Test
    public void testPostFilmWithInCorrectReleaseDateBadRequest() throws Exception {
        film.setReleaseDate(LocalDate.parse("1895-12-27"));
        String json = mapper.writeValueAsString(film);

        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andExpect(status().isBadRequest())
                .andExpect(jsonPath("message", Matchers.equalTo("Release date can be less than 28/12/1895")));
    }

    @Test
    public void testPostFilmWithPositiveDurationSuccess() throws Exception {
        film.setDuration(1);
        String json = mapper.writeValueAsString(film);

        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andExpect(status().isOk())
                .andExpect(jsonPath("name", Matchers.equalTo("Test test")));
    }

    @Test
    public void testPostFilmWithZeroDurationBadRequest() throws Exception {
        film.setDuration(0);
        String json = mapper.writeValueAsString(film);

        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andExpect(status().isBadRequest())
                .andExpect(jsonPath("message", Matchers.equalTo("Film Duration should be more than zero")));
    }


    @Test
    public void testPostFilmWithZeroNegativeDurationBadRequest() throws Exception {
        film.setDuration(-1);
        String json = mapper.writeValueAsString(film);

        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andExpect(status().isBadRequest())
                .andExpect(jsonPath("message", Matchers.equalTo("Film Duration should be more than zero")));
    }

    @Test
    public void testPutFilmCorrectDataSuccess() throws Exception {
        String json = mapper.writeValueAsString(film);
        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON)
                .content(json));
        film.setDescription("Upd");
        film.setDuration(12);
        film.setId(1L);
        json = mapper.writeValueAsString(film);

        mockMvc.perform(put("/films").contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andExpect(status().isOk())
                .andExpect(jsonPath("description", Matchers.equalTo("Upd")))
                .andExpect(jsonPath("duration", Matchers.equalTo(12)));
    }

    @Test
    public void testPutFilmSameNameDifferentReleaseDateBadRequest() throws Exception {
        String json = mapper.writeValueAsString(film);
        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON)
                .content(json));
        film.setDescription("Upd");
        film.setDuration(12);
        film.setReleaseDate(LocalDate.parse("1999-02-01"));
        json = mapper.writeValueAsString(film);

        mockMvc.perform(put("/films").contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andExpect(status().isNotFound())
                .andExpect(jsonPath("message", Matchers.equalTo("Film with specified name/releaseDate was not find.")));
    }

    @Test
    public void testPutFilmDifferentNameAndSameReleaseDateBadRequest() throws Exception {
        String json = mapper.writeValueAsString(film);
        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON)
                .content(json));
        film.setDescription("Upd");
        film.setDuration(12);
        film.setName("1999-02-01");
        json = mapper.writeValueAsString(film);

        mockMvc.perform(put("/films").contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andExpect(status().isNotFound())
                .andExpect(jsonPath("message", Matchers.equalTo("Film with specified name/releaseDate was not find.")));
    }

    @Test
    public void testGetUsersSuccess() throws Exception {
        Film film2 = new Film("Test test2", "Description", LocalDate.parse("1945-05-09"), 100);
        String json = mapper.writeValueAsString(film);
        String json2 = mapper.writeValueAsString(film2);

        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON)
                .content(json));
        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON)
                .content(json2));

        mockMvc.perform(get("/films")).andExpect(status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].name", Matchers.equalTo("Test test")))
                .andExpect(jsonPath("$[1].name", Matchers.equalTo("Test test2")));
    }
}