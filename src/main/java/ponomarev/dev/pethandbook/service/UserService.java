package ponomarev.dev.pethandbook.service;

import org.springframework.stereotype.Service;
import ponomarev.dev.pethandbook.model.UserDto;

import java.util.*;

@Service
public class UserService {


    private final Map<Long, UserDto> users;
    private final PetService petService;
    private Long idCounter;

    public UserService(PetService petService) {
        this.users = new HashMap<>();
        this.petService = petService;
        this.idCounter = 0L;
    }

    public UserDto findById(Long id) {
        var user = Optional.ofNullable(users.get(id)).orElseThrow(() -> new NoSuchElementException("User with id: %s not found".formatted(id)));
        user.setPets(petService.findAll(null, id));
        return user;
    }

    public List<UserDto> findAll(String name, String email, Integer age) {
        List<UserDto> userAll = users.values().stream()
                .filter(u -> name == null || name.equals(u.getName()))
                .filter(u -> email == null || email.equals(u.getEmail()))
                .filter(u -> age == null || u.getAge() < age)
                .toList();
        userAll.forEach(u -> u.setPets(petService.findAll(null, u.getId())));
        return userAll;
    }

    public UserDto create(UserDto userDto) {
        var newUser = new UserDto();
        newUser.setId(++idCounter);
        newUser.setName(userDto.getName());
        newUser.setEmail(userDto.getEmail());
        newUser.setAge(userDto.getAge());
        if(userDto.getPets() != null) {
            userDto.getPets().forEach(petService::createPet);
            newUser.setPets(petService.findAll(null, userDto.getId()));
        }
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    public UserDto update(Long id, UserDto userDto) {
        var oldUser = findById(id);
        oldUser.setName(userDto.getName());
        oldUser.setEmail(userDto.getEmail());
        oldUser.setAge(userDto.getAge());
        if(userDto.getPets() != null) {
            userDto.getPets().forEach(petService::createPet);
            oldUser.setPets(petService.findAll(null, id));
        }
        users.put(oldUser.getId(), oldUser);
        return oldUser;
    }

    public void delete(Long id) {
        findById(id);
        users.remove(id);
        var petsUsers = petService.findAll(null, id);
        petsUsers.forEach(petDto -> petService.deletePet(petDto.getId()));
    }



}
