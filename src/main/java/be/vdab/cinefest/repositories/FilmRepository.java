package be.vdab.cinefest.repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class FilmRepository {
    private final JdbcTemplate jdbcTemplate;

    public FilmRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public long findAantalVrijePlaatsen(){
        var sql = """
                select sum(vrijePlaatsen) as aantal
                from films
                """;
        return jdbcTemplate.queryForObject(sql, Long.class);
    }
}
