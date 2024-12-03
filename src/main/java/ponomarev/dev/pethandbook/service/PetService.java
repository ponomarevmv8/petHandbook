package ponomarev.dev.pethandbook.service;

import org.springframework.stereotype.Service;
import ponomarev.dev.pethandbook.mapper.PetMapper;
import ponomarev.dev.pethandbook.model.Pet;
import ponomarev.dev.pethandbook.model.PetDto;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PetService {

    private final Map<Long, Pet> petHandbook;

    private final PetMapper petMapper;

    private final AtomicLong idCounter = new AtomicLong(0L);

    public PetService(PetMapper petMapper) {
        this.petMapper = petMapper;
        this.petHandbook = new ConcurrentHashMap<>();
    }

    public PetDto createPet(PetDto petDto) {
        var newPet = new Pet(
                idCounter.incrementAndGet(),
                petDto.getName(),
                petDto.getUserId()
        );

        petHandbook.put(newPet.id(), newPet);
        return petMapper.toDto(newPet);
    }

    public PetDto findById(Long id) {
        return petMapper.toDto(Optional.ofNullable(petHandbook.get(id))
                .orElseThrow(() -> new NoSuchElementException("Pet with id: %s not found".formatted(id))));
    }

    public PetDto updatePet(Long id, PetDto petDto) {
        var oldPet = findById(id);
        var newPet = new Pet(
                oldPet.getId(),
                petDto.getName(),
                petDto.getUserId()
        );
        petHandbook.put(id, newPet);
        return petMapper.toDto(newPet);
    }

    public void deletePet(Long id) {
        findById(id);
        petHandbook.remove(id);
    }

    public List<PetDto> searchPet(String name, Long userId) {
        return petHandbook.values().stream()
                .filter(pet -> name == null || pet.name().equalsIgnoreCase(name))
                .filter(pet -> userId == null || pet.userId().equals(userId))
                .map(petMapper::toDto)
                .toList();
    }

    public List<PetDto> findAll() {
        return petHandbook.values()
                .stream()
                .map(petMapper::toDto)
                .toList();
    }


}
