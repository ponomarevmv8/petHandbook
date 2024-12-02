package ponomarev.dev.pethandbook.model;

public record User(
        Long id,
        String name,
        String email,
        Integer age
) {
}
