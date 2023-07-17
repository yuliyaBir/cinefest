package be.vdab.cinefest.services;

import be.vdab.cinefest.dto.IdTitelPlaatsenBesteld;
import be.vdab.cinefest.exceptions.ReservatieNietGevonden;
import be.vdab.cinefest.repositories.ReservatieRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservatieService {
    private final ReservatieRepository reservatieRepository;

    public ReservatieService(ReservatieRepository reservatieRepository) {
        this.reservatieRepository = reservatieRepository;
    }

    public List<IdTitelPlaatsenBesteld> findByEmailAdres(String emailAdres){
        var listReservaties = reservatieRepository.findByEmailAdres(emailAdres);
      if (listReservaties.stream().count() != 0){
          return listReservaties;
      } else{
          throw new ReservatieNietGevonden(emailAdres);
      }
    }
}
