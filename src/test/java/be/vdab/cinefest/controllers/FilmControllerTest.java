package be.vdab.cinefest.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Sql("/films.sql")
@AutoConfigureMockMvc
class FilmControllerTest extends AbstractTransactionalJUnit4SpringContextTests {
    private final static String FILMS = "films";
    private final static Path TEST_RESOURCES = Path.of("src/test/resources");
        private final MockMvc mockMvc;

    public FilmControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }
//    @Test
//    void findAantalVrijePlaatsen() throws Exception{
//        mockMvc.perform(get("/films/totaalVrijePlaatsen"))
//                .andExpectAll(
//                        status().isOk(),
//                        jsonPath("$").value(33));
//    }
    private long idVanTest1Film (){
        return jdbcTemplate.queryForObject("select id from films where titel = 'test1'", Long.class);
    }

    @Test
    void findById() throws Exception {
        var id = idVanTest1Film();
        mockMvc.perform(get("/films/{id}", id))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("id").value(id),
                        jsonPath("titel").value("test1"));
    }

    @Test
    void findByIdGeeftExceptionNotFoundBijEenOnbestaandeFilm() throws Exception {
        mockMvc.perform(get("/films/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void findAllFilms() throws Exception {
        mockMvc.perform(get("/films")).andExpectAll(
                status().isOk(),
                jsonPath("length()").value(
                        countRowsInTable(FILMS)));
    }

    @Test
    void findByJaar() throws Exception{
        mockMvc.perform(get("/films")
                .param("jaarIs", "2000")).andExpectAll(
                        status().isOk(),
                jsonPath("length()").value(
                        countRowsInTableWhere(FILMS,"jaar = 2000")));
    }

    @Test
    void deleteVerwijdertFilm() throws Exception {
        var id = idVanTest1Film();
        mockMvc.perform(delete("/films/{id}", id))
                .andExpect(status().isOk());
        assertThat(countRowsInTableWhere(FILMS, "id = " + id)).isZero();
    }

    @Test
    void create() throws Exception {
        var jsonData = Files.readString(
                TEST_RESOURCES.resolve("correcteFilm.json"));
        var responseBody = mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertThat(countRowsInTableWhere(FILMS,
                "titel = 'test3' and id = " + responseBody)).isOne();
    }
    @ParameterizedTest
    @ValueSource(strings = {"filmMetJaarBuitenGrenswaarde.json", "filmMetLeegJaar.json",
            "filmMetLegeTitel.json","filmZonderTitel.json", "filmZonderJaar.json"})
    void createMetVerkeerdeDataMislukt(String bestandNaam) throws Exception{
        var jsonData = Files.readString(TEST_RESOURCES.resolve(bestandNaam));
        mockMvc.perform(post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData))
                .andExpect(status().isBadRequest());
    }

    @Test
    void patchWijzigtTitel() throws Exception {
        var jsonData = Files.readString(TEST_RESOURCES.resolve("correcteTitelWijziging.json"));
        var id = idVanTest1Film();
        mockMvc.perform(patch("/films/{id}/titel", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData))
                .andExpect(status().isOk());
        assertThat(countRowsInTableWhere(FILMS, "titel = 'gewijzigd' and id = " + id)).isOne();
    }

//    @Test
//    void patchVanOnbestaandeFilmMislukt() throws Exception {
//        var jsonData = Files.readString(TEST_RESOURCES.resolve("correcteTitelWijziging.json"));
//        mockMvc.perform(patch("/films/{id}/titel", Long.MAX_VALUE)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(jsonData))
//                .andExpect(status().isNotFound());
//    }
@Test
void patchVanOnbestaandeFilmMislukt() throws Exception {
    var jsonData =
            Files.readString(TEST_RESOURCES.resolve("correcteTitelWijziging.json"));
    mockMvc.perform(patch("/films/{id}/titel", Long.MAX_VALUE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonData))
            .andExpect(status().isNotFound());
}
    @ParameterizedTest
    @ValueSource(strings = {"titelWijzigingMetLegeTitel.json", "titelWijzigingZonderTItel.json"})
    void patchMetVerkeerdeDataMislukt(String bestandNaam) throws Exception {
        var jsonData = Files.readString(TEST_RESOURCES.resolve(bestandNaam));
        var id = idVanTest1Film();
        mockMvc.perform(patch("/films/{id}/titel", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData))
                .andExpect(status().isBadRequest());
    }
}