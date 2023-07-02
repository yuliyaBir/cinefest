package be.vdab.cinefest.controllers;

import be.vdab.cinefest.domain.Film;
import be.vdab.cinefest.exceptions.FilmNietGevondenException;
import be.vdab.cinefest.services.FilmService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Stream;

@RestController
public class FilmController {
    private final FilmService filmService;
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }
    private record IdTitelJaarVrijePlaatsen(long id, String titel, int jaar, int vrijePlaatsen){
        IdTitelJaarVrijePlaatsen (Film film){
            this(film.getId(), film.getTitel(), film.getJaar(), film.getVrijePlaatsen());
        }
    }

    @GetMapping("films/totaalVrijePlaatsen")
    public int findAantalVrijePlaatsen(){
        return filmService.findAantalVrijePlaatsen();
    }
    @GetMapping("films/{id}")
    IdTitelJaarVrijePlaatsen findById(@PathVariable long id){
        return filmService.findById(id)
                .map(film -> new IdTitelJaarVrijePlaatsen(film))
                .orElseThrow(() -> new FilmNietGevondenException(id));
    }
    @GetMapping("films")
    Stream<IdTitelJaarVrijePlaatsen> findAll(){
        return filmService.findAll().stream()
                .map(film -> new IdTitelJaarVrijePlaatsen(film));
    }
}
