package lt.eif.viko.gtamaseviciute.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lt.eif.viko.gtamaseviciute.model.Actor;
import lt.eif.viko.gtamaseviciute.repository.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/actors")
@Tag(name = "Actors", description = "Operacijos su aktoriais")
public class ActorController {

    @Autowired
    private ActorRepository actorRepository;

    @Operation(summary = "Gauti visus aktorius")
    @GetMapping
    public List<Actor> getAllActors() {
        return actorRepository.findAll();
    }

    @Operation(summary = "Pridėti aktoriu")
    @PostMapping
    public Actor createActor(@RequestBody Actor actor) {
        return actorRepository.save(actor);
    }

    @Operation(summary = "Patikrinimas ar aktorius egzistuoja")
    @GetMapping("/exists/{id}")
    public boolean existsById(@PathVariable Long id) {
        return actorRepository.existsById(id);
    }

    @Operation(summary = "Gauti aktorių kiekį")
    @GetMapping("/count")
    public long countActors() {
        return actorRepository.count();
    }

    @Operation(summary = "Gauti aktoriu pagal ID")
    @GetMapping("/{id}")
    public ResponseEntity<Actor> getActorById(@PathVariable Long id) {
        return actorRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Ištrinti aktorių pagal ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActor(@PathVariable Long id) {
        if (actorRepository.existsById(id)) {
            actorRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Ieškoti aktorių pagal vardą, gimimo datą")
    @GetMapping("/search")
    public List<Actor> searchActors(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer birth_year
    ) {
        return actorRepository.findAll().stream()
                .filter(a -> name == null || a.getName().toLowerCase().contains(name.toLowerCase()))
                .filter(a -> birth_year == null || a.getBirth_year() == birth_year)
                .toList();
    }
}
