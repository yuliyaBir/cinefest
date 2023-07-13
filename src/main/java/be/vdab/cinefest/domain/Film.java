package be.vdab.cinefest.domain;

import be.vdab.cinefest.exceptions.PlaatsenNietGenoegException;

import java.math.BigDecimal;

public class Film {
    private final long id;
    private final String titel;
    private final int jaar;
    private int vrijePlaatsen;
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
    public void reserveer(int plaatsen){
        if (plaatsen <= 0){
            throw new IllegalArgumentException("Het aantal moet meer dan 0 zijn");
        }
        if(vrijePlaatsen < plaatsen){
            throw new PlaatsenNietGenoegException();
        }
        vrijePlaatsen -= plaatsen;
    }
}
