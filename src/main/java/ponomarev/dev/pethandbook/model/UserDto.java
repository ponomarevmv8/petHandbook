package ponomarev.dev.pethandbook.model;

import jakarta.validation.constraints.*;

import java.util.List;

public class UserDto {

    private Long id;

    @NotBlank(message = "name connot be blank")
    private String name;

    @NotBlank
    @Email(message = "email not correct")
    private String email;

    @NotNull
    @Min(value = 0, message = "Age must be positive")
    @Max(value = 120, message = "Age connot more then 120")
    private Integer age;

    private List<PetDto> pets;

    public UserDto() {
    }

    public UserDto(Long id, String name, String email, Integer age, List<PetDto> pets) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.pets = pets;
    }

    public UserDto(Long id, String name, String email, Integer age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public List<PetDto> getPets() {
        return pets;
    }

    public void setPets(List<PetDto> pets) {
        this.pets = pets;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", pets=" + pets +
                '}';
    }
}
