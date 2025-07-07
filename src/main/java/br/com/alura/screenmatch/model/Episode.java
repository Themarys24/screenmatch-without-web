package br.com.alura.screenmatch.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Entity
@Table(name = "episodes")
public class Episode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer season;
    private String title;
    private Integer number;
    private Double episodeReviews;
    private LocalDate releaseDate;

    @ManyToOne
    private Series serie;

    public Episode(){}

    public Episode(Integer numberSeason, EpisodeData episodeData) {
        this.season = numberSeason;
        this.title = episodeData.title();
        this.number = episodeData.number();
        try {
            this.episodeReviews = Double.valueOf(episodeData.episodeReviews());
        } catch (NumberFormatException | NullPointerException e) {
            this.episodeReviews = 0.0;
        }

        setReleaseDateFromString(episodeData.releaseDate());

        //this.releaseDate = LocalDate.parse(episodeData.releaseDate());
    }

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Double getEpisodeReviews() {
        return episodeReviews;
    }

    public void setEpisodeReviews(Double episodeReviews) {
        this.episodeReviews = episodeReviews;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Series getSerie() {
        return serie;
    }

    public void setSerie(Series serie) {
        this.serie = serie;
    }

    public void setReleaseDateFromString(String dateString) {
        if (dateString != null && !dateString.equals("N/A")) {
            try {
                this.releaseDate = LocalDate.parse(dateString);
            } catch (DateTimeParseException e) {
                this.releaseDate = null; // data inválida
            }
        } else {
            this.releaseDate = null; // data não disponível
        }
    }

    @Override
    public String toString() {
        String nota = (episodeReviews == null || episodeReviews == 0.0) ? "Informação indisponível" : String.valueOf(episodeReviews);
        String data = (releaseDate == null) ? "Informação indisponível" : releaseDate.toString();
        return "season=" + season +
                ", title='" + title + '\'' +
                ", number=" + number +
                ", episodeReviews=" + (episodeReviews != 0.0 ? episodeReviews : "Unavailable information") +
                ", releaseDate=" + (releaseDate != null ? releaseDate : "Unavailable information");
    }
}
