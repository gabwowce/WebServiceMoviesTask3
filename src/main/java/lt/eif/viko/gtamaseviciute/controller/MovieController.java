package lt.eif.viko.gtamaseviciute.controller;

import lt.eif.viko.gtamaseviciute.model.Movie;
import lt.eif.viko.gtamaseviciute.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
@Tag(name = "Movies", description = "Operacijos su filmais")
public class MovieController {

    @Autowired
    private MovieRepository movieRepository;

    @Operation(summary = "Gauti visus filmus")
    @GetMapping
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }


    @Operation(summary = "Sukurti filmą")
    @PostMapping
    public Movie createMovie(@RequestBody Movie movie) {
        return movieRepository.save(movie);
    }

    @Operation(summary = "Rasti filmą pagal žanrą")
    @GetMapping("/genre/{genre}")
    public List<Movie> getByGenre(@PathVariable String genre) {
        return movieRepository.findByGenre(genre);
    }

    @Operation(summary = "Patikrinimas ar filmas egzistuoja")
    @GetMapping("/exists/{id}")
    public boolean existsById(@PathVariable Long id) {
        return movieRepository.existsById(id);
    }

    @Operation(summary = "Gauti filmų kiekį")
    @GetMapping("/count")
    public long countMovies() {
        return movieRepository.count();
    }


    @Operation(summary = "Gauti filmą pagal ID")
    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        return movieRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @Operation(summary = "Ištrinti filmą pagal ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        if (movieRepository.existsById(id)) {
            movieRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Ieškoti filmų pagal pavadinimą, žanrą arba minimalų reitingą")
    @GetMapping("/search")
    public List<Movie> searchMovies(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Float minRating
    ) {
        return movieRepository.findAll().stream()
                .filter(m -> title == null || m.getTitle().toLowerCase().contains(title.toLowerCase()))
                .filter(m -> genre == null || m.getGenre().equalsIgnoreCase(genre))
                .filter(m -> minRating == null || m.getRating() >= minRating)
                .toList();
    }


}
