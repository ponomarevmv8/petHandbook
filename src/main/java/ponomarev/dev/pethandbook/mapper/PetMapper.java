package ponomarev.dev.pethandbook.mapper;

import org.springframework.stereotype.Component;
import ponomarev.dev.pethandbook.model.Pet;
import ponomarev.dev.pethandbook.model.PetDto;

@Component
public class PetMapper implements MapperCustomer<PetDto, Pet> {
    @Override
    public PetDto toDto(Pet entity) {
        return new PetDto(
                entity.id(),
                entity.name(),
                entity.userId()
        );
    }

    @Override
    public Pet toEntity(PetDto dto) {
        return new Pet(
                dto.getId(),
                dto.getName(),
                dto.getUserId()
        );
    }
}
