package be.vdab.cinefest.repositories;

import be.vdab.cinefest.domain.Film;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class FilmRepository {
    private final JdbcTemplate jdbcTemplate;

    public FilmRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public int findAantalVrijePlaatsen(){
        var sql = """
                select sum(vrijePlaatsen) as aantal
                from films
                """;
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
    private final RowMapper<Film> FilmMapper = (result, rowNum) ->
            new Film(result.getLong("id"),
                    result.getString("titel"),
                    result.getInt("jaar"),
                    result.getInt("vrijePlaatsen"),
                    result.getBigDecimal("aankoopprijs"));
    public Optional<Film> findById(long id){
        try {
            var sql = """
                select id, titel, jaar, vrijePlaatsen, aankoopprijs
                from films
                where id = ?
                """;
            return Optional.of(jdbcTemplate.queryForObject(sql, FilmMapper, id));
        } catch (IncorrectResultSizeDataAccessException ex){
            return Optional.empty();
        }
    }
    public List<Film> findAll(){
            var sql = """
                    select id, titel, jaar, vrijePlaatsen, aankoopprijs
                    from films
                    order by titel
                    """;
            return jdbcTemplate.query(sql, FilmMapper);
    }
}
