package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.models.User;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTests {

    private static ObjectMapper mapper;
    private MockMvc mockMvc;
    private User user;

    @InjectMocks
    private UserController userController;

    @BeforeAll
    public static void prepare() {
        mapper = new ObjectMapper();
    }

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).setControllerAdvice(new ExceptionApiHandler()).build();
        user = new User("user1", "user1@user1.com", LocalDate.parse("1945-05-09"));
        user.setName("u1");
    }

    @Test
    public void testGetUsersSuccess() throws Exception {
        User user2 = new User("user2", "user2@user2.com", LocalDate.parse("1945-10-09"));
        String json = mapper.writeValueAsString(user);
        String json2 = mapper.writeValueAsString(user2);

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                .content(json));
        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                .content(json2));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].name", Matchers.equalTo("u1")))
                .andExpect(jsonPath("$[0].id", Matchers.equalTo(1)))
                .andExpect(jsonPath("$[0].login", Matchers.equalTo("user1")))
                .andExpect(jsonPath("$[0].birthday", Matchers.equalTo("1945-05-09")))
                .andExpect(jsonPath("$[1].name", Matchers.equalTo("user2")))
                .andExpect(jsonPath("$[1].id", Matchers.equalTo(2)))
                .andExpect(jsonPath("$[1].login", Matchers.equalTo("user2")))
                .andExpect(jsonPath("$[1].birthday", Matchers.equalTo("1945-10-09")));
    }

    @Test
    public void testPostUsersCorrectDataSuccess() throws Exception {
        String json = mapper.writeValueAsString(user);

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andExpect(status().isOk())
                .andExpect(jsonPath("name", Matchers.equalTo("u1")));
    }

    @Test
    public void testPostUsersWithDuplicatedEmailBadRequest() throws Exception {
        String json = mapper.writeValueAsString(user);

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                .content(json));
        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("message", Matchers.equalTo("User account is already exist.")));
    }

    @Test
    public void testPostUsersFailEmailBadRequest() throws Exception {
        user.setEmail("user1user1.com");
        String json = mapper.writeValueAsString(user);

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message", Matchers.equalTo("Email should contain @ char")));
    }

    @Test
    public void testPostUsersEmptyEmailBadRequest() throws Exception {
        user.setEmail("");
        String json = mapper.writeValueAsString(user);

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message", Matchers.equalTo("Email can't be empty")));
    }

    @Test
    public void testPostUsersFutureBirthdayDateBadRequest() throws Exception {
        user.setBirthday(LocalDate.now().plusDays(1));
        String json = mapper.writeValueAsString(user);

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message", Matchers.equalTo("Birthday can be in the future")));
    }

    @Test
    public void testPostUsersTodayBirthdayDateSuccess() throws Exception {
        user.setBirthday(LocalDate.now());
        String json = mapper.writeValueAsString(user);

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void testPostUsersFailLoginBadRequest() throws Exception {
        user.setLogin("user 1");
        String json = mapper.writeValueAsString(user);

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message", Matchers.equalTo("Login can't contain whitespaces")));
    }

    @Test
    public void testPostUsersEmptyLoginBadRequest() throws Exception {
        user.setLogin("");
        String json = mapper.writeValueAsString(user);

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message", Matchers.equalTo("Login can't be empty")));
    }

    @Test
    public void testPutUsersCorrectDataSuccess() throws Exception {
        String json = mapper.writeValueAsString(user);
        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                .content(json));
        user.setName("Test");
        user.setLogin("TestLogin");
        user.setId(1L);
        json = mapper.writeValueAsString(user);

        mockMvc.perform(put("/users").contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andExpect(status().isOk())
                .andExpect(jsonPath("name", Matchers.equalTo("Test")))
                .andExpect(jsonPath("login", Matchers.equalTo("TestLogin")));
    }

    @Test
    public void testPutWrongUsersCorrectBadRequest() throws Exception {
        String json = mapper.writeValueAsString(user);
        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                .content(json));
        user.setName("Test");
        user.setLogin("TestLogin");
        user.setEmail("TestLogin@qwe.rt");
        user.setId(2L);
        json = mapper.writeValueAsString(user);

        mockMvc.perform(put("/users").contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andExpect(status().isNotFound())
                .andExpect(jsonPath("message", Matchers.equalTo("User with specified email is not find.")));
    }
}