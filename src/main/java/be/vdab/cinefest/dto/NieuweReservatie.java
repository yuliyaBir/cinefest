package be.vdab.cinefest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record NieuweReservatie (@NotBlank @Email String email, @Positive int aantal){
}
