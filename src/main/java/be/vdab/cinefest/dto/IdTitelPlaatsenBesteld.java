package be.vdab.cinefest.dto;

import java.time.LocalDateTime;

public record IdTitelPlaatsenBesteld(long id, String titel, int plaatsen, LocalDateTime besteld){
}
