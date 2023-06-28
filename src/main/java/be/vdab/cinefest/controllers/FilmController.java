package be.vdab.cinefest.controllers;

import be.vdab.cinefest.domain.Film;
import be.vdab.cinefest.exceptions.FilmNietGevondenException;
import be.vdab.cinefest.services.FilmService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FilmController {
    private final FilmService filmService;
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }
    @GetMapping("films/totaalVrijePlaatsen")
    public int findAantalVrijePlaatsen(){
        return filmService.findAantalVrijePlaatsen();
    }
    @GetMapping("films/{id}")
    Film findById(@PathVariable long id){
        return filmService.findById(id)
                .orElseThrow(() -> new FilmNietGevondenException(id));
    }
}
