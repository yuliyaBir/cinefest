package be.vdab.cinefest.domain;

import java.time.LocalDateTime;

public class Reservatie {
    private long id;
    private final long filmId;
    private final String emailAdres;
    private final int plaatsen;
    private final LocalDateTime besteld;

    public Reservatie(long id, int filmId, String emailAdres, int plaatsen, LocalDateTime besteld) {
        this.id = id;
        this.filmId = filmId;
        this.emailAdres = emailAdres;
        this.plaatsen = plaatsen;
        this.besteld = besteld;
    }

    public Reservatie(long filmId, String emailAdres, int plaatsen) {
        id = 0;
        this.filmId = filmId;
        this.emailAdres = emailAdres;
        this.plaatsen = plaatsen;
        besteld = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public long getFilmId() {
        return filmId;
    }

    public String getEmailAdres() {
        return emailAdres;
    }

    public int getPlaatsen() {
        return plaatsen;
    }

    public LocalDateTime getBesteld() {
        return besteld;
    }
}
