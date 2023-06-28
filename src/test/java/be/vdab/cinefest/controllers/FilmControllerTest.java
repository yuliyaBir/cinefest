package be.vdab.cinefest.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Sql("/films.sql")
@AutoConfigureMockMvc
class FilmControllerTest extends AbstractTransactionalJUnit4SpringContextTests {
    private final static String FILMS = "films";
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
                        jsonPath("titel").value("test1")
                );
    }
}