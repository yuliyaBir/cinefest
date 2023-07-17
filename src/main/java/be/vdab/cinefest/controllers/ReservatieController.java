package be.vdab.cinefest.controllers;

import be.vdab.cinefest.dto.IdTitelPlaatsenBesteld;
import be.vdab.cinefest.exceptions.ReservatieNietGevonden;
import be.vdab.cinefest.services.ReservatieService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@RestController
@RequestMapping("reservatie")
public class ReservatieController {
    private final ReservatieService reservatieService;

    public ReservatieController(ReservatieService reservatieService) {
        this.reservatieService = reservatieService;
    }

    @GetMapping(params = "emailAdres")
    List<IdTitelPlaatsenBesteld> findByEmailAdres(String emailAdres) {
            return reservatieService.findByEmailAdres(emailAdres);
    }
}
