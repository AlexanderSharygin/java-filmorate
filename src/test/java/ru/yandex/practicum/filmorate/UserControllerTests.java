package ru.yandex.practicum.filmorate;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import ru.yandex.practicum.filmorate.controllers.UserController;

@WebMvcTest(UserController.class)
public class UserControllerTests {
/*
    private static ObjectMapper mapper;

    private User user;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @BeforeAll
    public static void prepare() {
        mapper = new ObjectMapper();
    }

    @BeforeEach
    public void setup() {
        user = new User("user1", "user1@user1.com", LocalDate.parse("1945-05-09"));
        user.setName("u1");
    }

    @Test
    public void testGetUsersSuccess() throws Exception {
        User user2 = new User("user2", "user2@user2.com", LocalDate.parse("1945-10-09"));
        user2.setName("u2");
        user.setId(1L);
        user2.setId(2L);
        when(userService.getUsers())
                .thenReturn(List.of(user, user2));
        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].name", Matchers.equalTo("u1")))
                .andExpect(jsonPath("$[0].id", Matchers.equalTo(1)))
                .andExpect(jsonPath("$[0].login", Matchers.equalTo("user1")))
                .andExpect(jsonPath("$[0].birthday", Matchers.equalTo("1945-05-09")))
                .andExpect(jsonPath("$[1].name", Matchers.equalTo("u2")))
                .andExpect(jsonPath("$[1].id", Matchers.equalTo(2)))
                .andExpect(jsonPath("$[1].login", Matchers.equalTo("user2")))
                .andExpect(jsonPath("$[1].birthday", Matchers.equalTo("1945-10-09")));
    }

   /* @Test
    public void testPostUsersCorrectDataSuccess() throws Exception {
        String json = mapper.writeValueAsString(user);
        when(userService.addUser(user)).thenReturn(user);
        mockMvc.perform(MockMvcRequestBuilders.post("/users").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", Matchers.equalTo("u1")));
    }

    @Test
    public void testPostUsersFailEmailBadRequest() throws Exception {
        user.setEmail("user1user1.com");
        String json = mapper.writeValueAsString(user);
        when(userService.addUser(user)).thenReturn(user);
        mockMvc.perform(MockMvcRequestBuilders.post("/users").contentType(MediaType.APPLICATION_JSON).content(json));
        mockMvc.perform(MockMvcRequestBuilders.post("/users").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testPostUsersEmptyEmailBadRequest() throws Exception {
        user.setEmail("");
        String json = mapper.writeValueAsString(user);
        when(userService.addUser(user)).thenReturn(user);
        mockMvc.perform(MockMvcRequestBuilders.post("/users").contentType(MediaType.APPLICATION_JSON).content(json));
        mockMvc.perform(MockMvcRequestBuilders.post("/users").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testPostUsersFutureBirthdayDateBadRequest() throws Exception {
        user.setBirthday(LocalDate.now().plusDays(1));
        String json = mapper.writeValueAsString(user);
        when(userService.addUser(user)).thenReturn(user);
        mockMvc.perform(MockMvcRequestBuilders.post("/users").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testPostUsersTodayBirthdayDateSuccess() throws Exception {
        user.setBirthday(LocalDate.now());
        String json = mapper.writeValueAsString(user);
        when(userService.addUser(user)).thenReturn(user);
        mockMvc.perform(MockMvcRequestBuilders.post("/users").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void testPostUsersFailLoginBadRequest() throws Exception {
        user.setLogin("user 1");
        String json = mapper.writeValueAsString(user);
        when(userService.addUser(user)).thenReturn(user);
        mockMvc.perform(MockMvcRequestBuilders.post("/users").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testPostUsersEmptyLoginBadRequest() throws Exception {
        user.setLogin("");
        String json = mapper.writeValueAsString(user);
        when(userService.addUser(user)).thenReturn(user);
        mockMvc.perform(MockMvcRequestBuilders.post("/users").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testPutUsersCorrectDataSuccess() throws Exception {
        String json = mapper.writeValueAsString(user);
        when(userService.addUser(user)).thenReturn(user);
        mockMvc.perform(MockMvcRequestBuilders.post("/users").contentType(MediaType.APPLICATION_JSON).content(json));
        user.setName("Test");
        user.setLogin("TestLogin");
        user.setUserId(1L);
        json = mapper.writeValueAsString(user);
        mockMvc.perform(MockMvcRequestBuilders.put("/users").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }*/
}