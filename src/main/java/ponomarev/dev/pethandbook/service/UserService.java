package ponomarev.dev.pethandbook.service;

import org.springframework.stereotype.Service;
import ponomarev.dev.pethandbook.mapper.UserMapper;
import ponomarev.dev.pethandbook.model.User;
import ponomarev.dev.pethandbook.model.UserDto;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService {


    private final Map<Long, User> users;
    private final PetService petService;
    private final UserMapper userMapper;
    private final AtomicLong idCounter = new AtomicLong(0L);

    public UserService(PetService petService, UserMapper userMapper) {
        this.userMapper = userMapper;
        this.users = new ConcurrentHashMap<>();
        this.petService = petService;
    }

    public UserDto findById(Long id) {
        return userMapper.toDto(Optional.ofNullable(users.get(id))
                .orElseThrow(() -> new NoSuchElementException("User with id: %s not found".formatted(id))));
    }

    public List<UserDto> search(String name, String email, Integer ageMax) {
        return users.values().stream()
                .filter(u -> name == null || name.equals(u.name()))
                .filter(u -> email == null || email.equals(u.email()))
                .filter(u -> ageMax == null || u.age() < ageMax)
                .map(userMapper::toDto)
                .toList();
    }

    public UserDto create(UserDto userDto) {
        var newUser = new User(
                idCounter.incrementAndGet(),
                userDto.getName(),
                userDto.getEmail(),
                userDto.getAge()
        );
        users.put(newUser.id(), newUser);
        return userMapper.toDto(newUser);
    }

    public UserDto update(Long id, UserDto userDto) {
        findById(id);
        var newUser = new User(
                id,
                userDto.getName(),
                userDto.getEmail(),
                userDto.getAge()
        );
        users.put(newUser.id(), newUser);
        return userMapper.toDto(newUser);
    }

    public void delete(Long id) {
        findById(id);
        users.remove(id);
        var petsUsers = petService.searchPet(null, id);
        petsUsers.forEach(petDto -> petService.deletePet(petDto.getId()));
    }


    public List<UserDto> findAll() {
        return users.values()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }
}
