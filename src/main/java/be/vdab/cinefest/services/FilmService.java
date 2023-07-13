package be.vdab.cinefest.services;

import be.vdab.cinefest.domain.Film;
import be.vdab.cinefest.domain.Reservatie;
import be.vdab.cinefest.dto.NieuweFilm;
import be.vdab.cinefest.dto.NieuweReservatie;
import be.vdab.cinefest.exceptions.FilmNietGevondenException;
import be.vdab.cinefest.exceptions.PlaatsenNietGenoegException;
import be.vdab.cinefest.repositories.FilmRepository;
import be.vdab.cinefest.repositories.ReservatieRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class FilmService {
    private final FilmRepository filmRepository;
    private final ReservatieRepository reservatieRepository;

    public FilmService(FilmRepository filmRepository, ReservatieRepository reservatieRepository) {
        this.filmRepository = filmRepository;
        this.reservatieRepository = reservatieRepository;
    }
    public int findAantalVrijePlaatsen(){
        return filmRepository.findAantalVrijePlaatsen();
    }
    public Optional<Film> findById(long id){
        return filmRepository.findById(id);
    }
    public List<Film> findAll(){
        return filmRepository.findAll();
    }
    public List<Film> findByJaar(int jaar){
        return filmRepository.findByJaar(jaar);
    }
    @Transactional
    public void delete (long id){
        filmRepository.delete(id);
    }
    @Transactional
    public long create (NieuweFilm nieuweFilm){
        var film = new Film(nieuweFilm.titel(), nieuweFilm.jaar(),0, BigDecimal.ZERO);
        return filmRepository.create(film);
    }
    @Transactional
    public void updateTitel (long id, String nieuweTitel){
        filmRepository.updateTitel(id, nieuweTitel);
    }
    @Transactional
    public long reserveer(long filmId, NieuweReservatie nieuweReservatie){
        var reservatie = new Reservatie(filmId, nieuweReservatie.email(), nieuweReservatie.aantal());
        var film = filmRepository.findAndLockById(filmId).orElseThrow(() -> new FilmNietGevondenException(filmId));
        film.reserveer(nieuweReservatie.aantal());
        try{
            filmRepository.updateAantalTickets(filmId, nieuweReservatie.aantal());
        }
        catch (PlaatsenNietGenoegException ex){
            ex.getMessage();
        }
        return reservatieRepository.create(reservatie);
    }
}
