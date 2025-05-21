package lt.eif.viko.gtamaseviciute.repository;

import lt.eif.viko.gtamaseviciute.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByGenre(String genre);
    List<Movie> findByRatingGreaterThan(float rating);
}