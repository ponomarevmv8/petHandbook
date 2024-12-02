package ponomarev.dev.pethandbook.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ponomarev.dev.pethandbook.model.UserDto;
import ponomarev.dev.pethandbook.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable Long id) {
        log.info("Find user by id: {}", id);
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDto>> searchUser(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Integer age
    ) {
        log.info("Search all users");
        return ResponseEntity.ok(userService.search(name, email, age));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> findAll() {
        log.info("Find all users");
        return ResponseEntity.ok(userService.findAll());
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody @Valid UserDto userDto) {
        log.info("Create user: {}", userDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.create(userDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(
            @PathVariable Long id,
            @RequestBody @Valid UserDto userDto) {
        log.info("Update user: {}", userDto);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(userService.update(id,userDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Delete user: {}", id);
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
