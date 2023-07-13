package be.vdab.cinefest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class PlaatsenNietGenoegException extends RuntimeException{
    public PlaatsenNietGenoegException() {
        super("Aantal beschikbare plaatsen niet genoeg");
    }
}
