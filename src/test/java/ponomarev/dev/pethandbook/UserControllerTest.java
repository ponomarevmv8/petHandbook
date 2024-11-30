package ponomarev.dev.pethandbook;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ponomarev.dev.pethandbook.model.UserDto;
import ponomarev.dev.pethandbook.service.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldCreateUser() throws Exception {
        var newUser = new UserDto(
                null,
                "Max",
                "max@mail.ru",
                28
        );

        String newUserJson = objectMapper.writeValueAsString(newUser);

        String createUserJson = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newUserJson)
        )
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        var createUser = objectMapper.readValue(createUserJson, UserDto.class);

        Assertions.assertNotNull(createUser.getId());
        Assertions.assertAll(
                () -> Assertions.assertEquals(newUser.getName(), createUser.getName()),
                () -> Assertions.assertEquals(newUser.getEmail(), createUser.getEmail()),
                () -> Assertions.assertEquals(newUser.getAge(), createUser.getAge())
        );
    }

    @Test
    void shouldNotCreateUserWhenNotValid() throws Exception {
        var newUser = new UserDto(
                1L,
                "Max",
                "max@mail.ru",
                28
        );
        String newUserJson = objectMapper.writeValueAsString(newUser);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newUserJson)
        ).andExpect(status().isBadRequest());

        var newUserBlankName = new UserDto(
                null,
                " ",
                "max@mail.ru",
                28
        );
        newUserJson = objectMapper.writeValueAsString(newUser);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newUserJson)
        ).andExpect(status().isBadRequest());

        var newUserNotValidEmail = new UserDto(
                null,
                "Max",
                "maxmail.ru",
                28
        );
        newUserJson = objectMapper.writeValueAsString(newUser);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newUserJson)
        ).andExpect(status().isBadRequest());

        var newUserNotPositiveAge = new UserDto(
                null,
                "Max",
                "max@mail.ru",
                -28
        );
        newUserJson = objectMapper.writeValueAsString(newUser);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newUserJson)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void shouldFindUser() throws Exception {
        var newUser = new UserDto(
                null,
                "Max",
                "max@mail.ru",
                28
        );

        newUser = userService.create(newUser);

        String findUserJson = mockMvc.perform(get("/users/{userId}", newUser.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        var findUser = objectMapper.readValue(findUserJson, UserDto.class);

        org.assertj.core.api.Assertions.assertThat(newUser)
                .usingRecursiveComparison()
                .isEqualTo(findUser);
    }

    @Test
    void shouldNotFindUserWhenNotValid() throws Exception {
        mockMvc.perform(get("/users/{userId}", Integer.MAX_VALUE))
                .andExpect(status().isBadRequest());
    }

}
