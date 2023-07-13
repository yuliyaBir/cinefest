package be.vdab.cinefest.controllers;

import be.vdab.cinefest.domain.Film;
import be.vdab.cinefest.domain.Reservatie;
import be.vdab.cinefest.dto.NieuweFilm;
import be.vdab.cinefest.dto.NieuweReservatie;
import be.vdab.cinefest.exceptions.FilmNietGevondenException;
import be.vdab.cinefest.services.FilmService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.web.bind.annotation.*;

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
    private record TitelWijziging(@NotBlank String titel){}
    //private record ReservatieToevoeging (@NotNull @Positive long filmId, @NotBlank @Email String email, @NotNull @Positive int aantal){}
    private record EmailToevoeging(@Email String email){}
    private record AantalPlaatsenToevoeging(@NotNull @Positive int aantal){}
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
    @GetMapping(value = "films", params = "jaarIs")
    Stream<IdTitelJaarVrijePlaatsen> findByJaar(int jaarIs){
        return filmService.findByJaar(jaarIs)
                .stream()
                .map(film -> new IdTitelJaarVrijePlaatsen(film));
    }
    @DeleteMapping("films/{id}")
    void delete (@PathVariable long id){
        filmService.delete(id);
    }
    @PostMapping("films")
    long create (@RequestBody @Valid NieuweFilm nieuweFilm){
        return filmService.create(nieuweFilm);
    }
    @PatchMapping("films/{id}/titel")
    void updateTitel (@PathVariable long id, @RequestBody @Valid TitelWijziging wijziging){
        filmService.updateTitel(id, wijziging.titel());
    }
    @PostMapping("films/{id}/reservatie")
    long reserveer(@PathVariable long id, @RequestBody @Valid NieuweReservatie nieuweReservatie){
        return filmService.reserveer(id, nieuweReservatie);
    }
}
