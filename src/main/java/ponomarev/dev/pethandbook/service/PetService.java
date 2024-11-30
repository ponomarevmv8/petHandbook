package ponomarev.dev.pethandbook.service;

import org.springframework.stereotype.Service;
import ponomarev.dev.pethandbook.model.PetDto;

import java.util.*;

@Service
public class PetService {

    private final Map<Long, PetDto> petHandbook;

    private Long idCounter;

    public PetService() {
        this.petHandbook = new HashMap<>();
        idCounter = 0L;
    }

    public PetDto createPet(PetDto petDto) {
        var newPet = new PetDto(
                ++idCounter,
                petDto.getName(),
                petDto.getUserId()
        );

        petHandbook.put(newPet.getId(), newPet);
        return newPet;
    }

    public PetDto findById(Long id) {
        return Optional.ofNullable(petHandbook.get(id))
                .orElseThrow(() -> new NoSuchElementException("Pet with id: %s not found".formatted(id)));
    }

    public List<PetDto> findPetsByUserId(Long userId) {
        return petHandbook.values().stream()
                .filter(petDto -> petDto.getUserId().equals(userId))
                .toList();
    }

    public PetDto updatePet(Long id, PetDto petDto) {
        var oldPet = findById(id);
        var newPet = new PetDto(
                oldPet.getId(),
                petDto.getName(),
                petDto.getUserId()
        );
        petHandbook.put(id, newPet);
        return newPet;
    }

    public void deletePet(Long id) {
        findById(id);
        petHandbook.remove(id);
    }

    public List<PetDto> findAll(String name, Long userId) {
        return petHandbook.values().stream()
                .filter(petDto -> name == null || petDto.getName().equalsIgnoreCase(name))
                .filter(petDto -> userId == null || petDto.getUserId().equals(userId))
                .toList();
    }


}
