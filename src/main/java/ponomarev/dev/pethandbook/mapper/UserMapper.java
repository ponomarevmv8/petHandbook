package ponomarev.dev.pethandbook.mapper;

import org.springframework.stereotype.Component;
import ponomarev.dev.pethandbook.model.User;
import ponomarev.dev.pethandbook.model.UserDto;
import ponomarev.dev.pethandbook.service.PetService;

@Component
public class UserMapper implements MapperCustomer<UserDto, User> {

    private final PetService petService;

    public UserMapper(PetService petService) {
        this.petService = petService;
    }

    @Override
    public UserDto toDto(User entity) {
        UserDto userDto = new UserDto(
                entity.id(),
                entity.name(),
                entity.email(),
                entity.age(),
                petService.searchPet(null, entity.id())
        );
        return userDto;
    }

    @Override
    public User toEntity(UserDto dto) {
        return new User(
                dto.getId(),
                dto.getName(),
                dto.getEmail(),
                dto.getAge()
        );
    }
}
