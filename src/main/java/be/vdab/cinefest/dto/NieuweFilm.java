package be.vdab.cinefest.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record NieuweFilm(@NotBlank String titel, @Min(1000) @Max(9999) int jaar) {
}
