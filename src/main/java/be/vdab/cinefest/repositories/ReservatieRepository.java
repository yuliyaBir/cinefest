package be.vdab.cinefest.repositories;

import be.vdab.cinefest.domain.Reservatie;
import be.vdab.cinefest.dto.IdTitelPlaatsenBesteld;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ReservatieRepository {
    private final JdbcTemplate jdbcTemplate;

    public ReservatieRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    private RowMapper<IdTitelPlaatsenBesteld> idTitelPlaatsenBesteldMapper = (result, rowNUm) ->
            new IdTitelPlaatsenBesteld(result.getLong("reservatieId"),
                    result.getString("titel"),
                    result.getInt("plaatsen"),
                    result.getObject("besteld", LocalDateTime.class));


    public long create (Reservatie reservatie){
        var sql = """
                insert into reservaties(filmId, emailAdres, plaatsen, besteld)
                values(?,?,?,?)
                """;
        var keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            var statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setLong(1, reservatie.getFilmId());
            statement.setString(2, reservatie.getEmailAdres());
            statement.setInt(3, reservatie.getPlaatsen());
            statement.setObject(4, reservatie.getBesteld());
        return statement;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }
    public List<IdTitelPlaatsenBesteld> findByEmailAdres(String emailAdres){
        var sql = """
                select r.id as reservatieId, titel, plaatsen, besteld
                from films inner join reservaties r on films.id = r.filmId
                where emailAdres = ?
                order by reservatieId desc
                """;
        return jdbcTemplate.query(sql, idTitelPlaatsenBesteldMapper, emailAdres);
    }
}
