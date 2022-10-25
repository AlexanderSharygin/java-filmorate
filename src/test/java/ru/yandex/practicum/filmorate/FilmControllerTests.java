package ru.yandex.practicum.filmorate;

/*
@WebMvcTest(FilmController.class)
public class FilmControllerTests {

    private static ObjectMapper mapper;

    private Film film;

    @InjectMocks
    private FilmController filmController;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FilmService filmService;

    @BeforeAll
    public static void prepare() {
        mapper = new ObjectMapper();
    }

    @BeforeEach
    public void setup() {
        film = new Film("Test test", "Description", LocalDate.parse("1945-05-09"), 100);
    }

    @Test
    public void testPostFilmCorrectDataSuccess() throws Exception {
        String json = mapper.writeValueAsString(film);
        when(filmService.addFilm(film)).thenReturn(film);
        mockMvc.perform(MockMvcRequestBuilders.post("/films").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", Matchers.equalTo("Test test")));
    }


    @Test
    public void testPostFilmWith201CharsDescriptionBadRequest() throws Exception {
        film.setDescription(RandomStringUtils.randomAlphabetic(201));
        String json = mapper.writeValueAsString(film);
        when(filmService.addFilm(film)).thenReturn(film);
        mockMvc.perform(MockMvcRequestBuilders.post("/films").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testPostFilmWith200CharsDescriptionSuccess() throws Exception {
        film.setDescription(RandomStringUtils.randomAlphabetic(200));
        String json = mapper.writeValueAsString(film);
        when(filmService.addFilm(film)).thenReturn(film);
        mockMvc.perform(MockMvcRequestBuilders.post("/films").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void testPostFilmWithCorrectReleaseDateSuccess() throws Exception {
        film.setReleaseDate(LocalDate.parse("1895-12-28"));
        String json = mapper.writeValueAsString(film);
        when(filmService.addFilm(film)).thenReturn(film);
        mockMvc.perform(MockMvcRequestBuilders.post("/films").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void testPostFilmWithPositiveDurationSuccess() throws Exception {
        film.setDuration(1);
        String json = mapper.writeValueAsString(film);
        when(filmService.addFilm(film)).thenReturn(film);
        mockMvc.perform(MockMvcRequestBuilders.post("/films").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void testPostFilmWithZeroDurationBadRequest() throws Exception {
        film.setDuration(0);
        String json = mapper.writeValueAsString(film);
        when(filmService.addFilm(film)).thenReturn(film);
        mockMvc.perform(MockMvcRequestBuilders.post("/films").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testPostFilmWithZeroNegativeDurationBadRequest() throws Exception {
        film.setDuration(-1);
        String json = mapper.writeValueAsString(film);
        when(filmService.addFilm(film)).thenReturn(film);
        mockMvc.perform(MockMvcRequestBuilders.post("/films").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testPutFilmCorrectDataSuccess() throws Exception {
        String json = mapper.writeValueAsString(film);
        when(filmService.addFilm(film)).thenReturn(film);
        mockMvc.perform(MockMvcRequestBuilders.post("/films").contentType(MediaType.APPLICATION_JSON).content(json));
        film.setDescription("Upd");
        film.setDuration(12);
        film.setId(1L);
        json = mapper.writeValueAsString(film);
        mockMvc.perform(MockMvcRequestBuilders.put("/films").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk());
    }

    @Test
    public void testGetUsersSuccess() throws Exception {
        Film film2 = new Film("Test test2", "Description", LocalDate.parse("1945-05-09"), 100);
        when(filmService.getAll())
                .thenReturn(List.of(film, film2));
        mockMvc.perform(MockMvcRequestBuilders.get("/films"))
                .andExpect(status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].name", Matchers.equalTo("Test test")))
                .andExpect(jsonPath("$[1].name", Matchers.equalTo("Test test2")));
    }
}*/