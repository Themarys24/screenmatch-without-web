package br.com.alura.screenmatch.repository;

import br.com.alura.screenmatch.model.Category;
import br.com.alura.screenmatch.model.Episode;
import br.com.alura.screenmatch.model.Series;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Series, Long> {

    Optional<Series> findByTitleContainingIgnoreCase(String serieName);

    List<Series> findByActorsContainingIgnoreCaseAndRatingGreaterThanEqual(String nameActor, Double rating);

    List<Series> findTop5ByOrderByRatingDesc();

    List<Series> findByGenre(Category category);

    List<Series> findByTotalSeasonsLessThanEqualAndRatingGreaterThanEqual(Integer totalSeasons, Double rating);

    @Query( "SELECT s FROM Series s WHERE s.totalSeasons <= :totalSeasons AND s.rating >= :rating")
    List<Series> seriesBySeasonAndRating(Integer totalSeasons, Double rating);

    @Query("SELECT e FROM Series s JOIN s.episodios e WHERE LOWER(e.title) LIKE LOWER(CONCAT('%',:excerptEpisode, '%'))")
    List<Episode> episodesByexcerpt(String excerptEpisode);

   @Query("SELECT e FROM Series s JOIN s.episodios e WHERE s = :serie ORDER BY e.episodeReviews DESC LIMIT 5")
    List<Episode> topEpisodesBySeries(@Param("serie")Series serie);


    @Query("""
    SELECT e 
    FROM Series s 
    JOIN s.episodios e 
    WHERE s = :serie 
    AND EXTRACT(YEAR FROM e.releaseDate) >= :releaseYear
""")
   List<Episode> episodeBySerieAndYear(@Param("serie") Series serie, @Param("releaseYear") int releaseYear);
}