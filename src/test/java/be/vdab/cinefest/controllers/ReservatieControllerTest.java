package be.vdab.cinefest.controllers;

import be.vdab.cinefest.dto.IdTitelPlaatsenBesteld;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Path;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.assertj.core.api.Assertions.*;
@SpringBootTest
@Sql({"/films.sql","/reservaties.sql"})
@AutoConfigureMockMvc
class ReservatieControllerTest extends AbstractTransactionalJUnit4SpringContextTests {
    private final MockMvc mockMvc;
    private static final String FILMS = "films";
    private static final String RESERVATIES = "reservaties";
    private static final Path TEST_RESOURCES = Path.of("/src/test/resources");


    public ReservatieControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }
    private long idVanTest1Film (){
        return jdbcTemplate.queryForObject("select id from films where naam = 'test1'", Long.class);
    }
    @Test
    void findByEmailAdres() throws Exception {
        var aantalReservaties = countRowsInTableWhere(RESERVATIES, "emailAdres = 'test@example.org'");
        mockMvc.perform(get("/reservatie?emailAdres={emailAdres}", "test@example.org"))
//                .param("emailAdres", "test@example.org"))
                .andExpectAll(status().isOk(),
                        jsonPath("length()").value(aantalReservaties));

    }
}