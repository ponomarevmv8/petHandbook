package ponomarev.dev.pethandbook.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ponomarev.dev.pethandbook.model.PetDto;
import ponomarev.dev.pethandbook.service.PetService;

import java.util.List;

@RestController
@RequestMapping("/pets")
public class PetController {

    private static final Logger log = LoggerFactory.getLogger(PetController.class);
    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetDto> findById(@PathVariable Long id) {
        log.info("Find pet by id: {}", id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(petService.findById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<PetDto>> serchPet(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long userId
    ) {
        log.info("Find pets by name: {}, userId: {}", name, userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(petService.searchPet(name, userId));
    }

    @GetMapping
    public ResponseEntity<List<PetDto>> findAll() {
        log.info("Find all pets");
        return ResponseEntity.status(HttpStatus.OK)
                .body(petService.findAll());
    }

    @PostMapping
    public ResponseEntity<PetDto> create(
            @RequestBody @Valid PetDto petDto) {
        log.info("Create pet: {}", petDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(petService.createPet(petDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetDto> update(
            @PathVariable Long id,
            @RequestBody @Valid PetDto petDto) {
        log.info("Update pet: {}", petDto);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(petService.updatePet(id, petDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Delete pet: {}", id);
        petService.deletePet(id);
        return ResponseEntity.noContent().build();
    }

}
