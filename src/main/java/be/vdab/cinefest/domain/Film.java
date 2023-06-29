package be.vdab.cinefest.domain;

import java.math.BigDecimal;

public class Film {
    private final long id;
    private final String titel;
    private final int jaar;
    private final int vrijePlaatsen;
    private final BigDecimal aankoopprijs;

    public Film(long id, String titel, int jaar, int vrijePlaatsen, BigDecimal aankoopPrijs) {
        this.id = id;
        this.titel = titel;
        this.jaar = jaar;
        this.vrijePlaatsen = vrijePlaatsen;
        this.aankoopprijs = aankoopPrijs;
    }

    public Film(String titel, int jaar, int vrijePlaatsen, BigDecimal aankoopPrijs) {
        this(0, titel, jaar, vrijePlaatsen, aankoopPrijs);
    }

    public long getId() {
        return id;
    }

    public String getTitel() {
        return titel;
    }

    public int getJaar() {
        return jaar;
    }

    public int getVrijePlaatsen() {
        return vrijePlaatsen;
    }

    public BigDecimal getAankoopprijs() {
        return aankoopprijs;
    }
}
