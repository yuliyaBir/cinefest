package be.vdab.cinefest.domain;

import be.vdab.cinefest.exceptions.PlaatsenNietGenoegException;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class FilmTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Test
    void reserveerMetVoldoendePlaatsenGelukt() {
        var film =  new Film("test1", 1992, 3, BigDecimal.ONE);
        film.reserveer(2);
        assertThat(film.getVrijePlaatsen()).isOne();
    }
    @Test
    void reserveerMetOnvoldoendePlaatsenMislukt() {
        var film =  new Film("test1", 1992, 3, BigDecimal.ONE);
        assertThatExceptionOfType(PlaatsenNietGenoegException.class).isThrownBy(() -> film.reserveer(4));
    }
    @Test
    void reserveerMetAantalPlaatsenMinderDan0Mislukt() {
        var film =  new Film("test1", 1992, 3, BigDecimal.ONE);
        assertThatIllegalArgumentException().isThrownBy(() -> film.reserveer(-5));
    }

}