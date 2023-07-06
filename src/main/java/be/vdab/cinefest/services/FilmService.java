package be.vdab.cinefest.services;

import be.vdab.cinefest.domain.Film;
import be.vdab.cinefest.repositories.FilmRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class FilmService {
    private final FilmRepository filmRepository;

    public FilmService(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
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
}
