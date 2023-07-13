package be.vdab.cinefest.repositories;

import be.vdab.cinefest.domain.Film;
import be.vdab.cinefest.exceptions.FilmNietGevondenException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
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
    private final RowMapper<Film> filmMapper = (result, rowNum) ->
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
            return Optional.of(jdbcTemplate.queryForObject(sql, filmMapper, id));
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
            return jdbcTemplate.query(sql, filmMapper);
    }
    public List<Film> findByJaar(int jaar){
        var sql = """
                select id, titel, jaar, vrijePlaatsen, aankoopprijs
                from films
                where jaar = ?
                order by titel                
                """;
        return jdbcTemplate.query(sql, filmMapper, jaar);
    }
    public void delete(long id){
        var sql = """
                delete from films
                where id = ?
                """;
        jdbcTemplate.update(sql, id);
    }

    public long create(Film film) {
        var sql = """
                insert into films (titel, jaar, vrijePlaatsen, aankoopprijs)
                values (?,?,?,?)
                """;
        var keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            var statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, film.getTitel());
            statement.setInt(2, film.getJaar());
            statement.setInt(3, film.getVrijePlaatsen());
            statement.setBigDecimal(4, film.getAankoopprijs());
            return statement;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }
    public void updateTitel(long id, String nieuweTitel){
        var sql = """
                update films
                set titel = ?
                where id = ?
                """;
        if (jdbcTemplate.update(sql, nieuweTitel, id) == 0){
            throw new FilmNietGevondenException(id);
        }
    }
    public void updateAantalTickets(long id, int aantal){
        var sql = """
                update films
                set vrijePlaatsen = vrijePlaatsen - ?
                where id = ?
                """;
        if (jdbcTemplate.update(sql, aantal, id) == 0){
            throw new FilmNietGevondenException(id);
        }
    }
    public Optional<Film> findAndLockById(long id){
        try {
            var sql = """
                select id, titel, jaar, vrijePlaatsen, aankoopprijs
                from films
                where id = ?
                for update
                """;
            return Optional.of(jdbcTemplate.queryForObject(sql, filmMapper, id));
        } catch (IncorrectResultSizeDataAccessException ex){
            return Optional.empty();
        }
    }
}
