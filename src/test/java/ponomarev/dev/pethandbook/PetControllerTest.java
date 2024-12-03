package ponomarev.dev.pethandbook;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ponomarev.dev.pethandbook.model.PetDto;
import ponomarev.dev.pethandbook.service.PetService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PetService petService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldCreatePet() throws Exception {

        var newPet = new PetDto(
                null,
                "Rex",
                1L
        );

        String newPetJson = objectMapper.writeValueAsString(newPet);

        String createPetJson = mockMvc.perform(post("/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newPetJson)
        ).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();

        PetDto createPetDto = objectMapper.readValue(createPetJson, PetDto.class);

        Assertions.assertNotNull(createPetDto.getId());
        Assertions.assertAll(
                () -> Assertions.assertEquals(newPet.getName(), createPetDto.getName()),
                () -> Assertions.assertEquals(newPet.getUserId(), createPetDto.getUserId())
        );
    }

    @Test
    void shouldNotCreatePetWhenNameIsBlank() throws Exception {
        var newPetIdBlankName = new PetDto(
                null,
                " ",
                1L
        );

        String newPetJson = objectMapper.writeValueAsString(newPetIdBlankName);

        mockMvc.perform(post("/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newPetJson)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void shouldBotCreatePetWhenUserIdIsNull() throws Exception {
        var newPetIdNullUserId = new PetDto(
                null,
                "Rex",
                null
        );

        String newPetJson = objectMapper.writeValueAsString(newPetIdNullUserId);

        mockMvc.perform(post("/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newPetJson)
        ).andExpect(status().isBadRequest());

    }

    @Test
    void shouldFindPetById() throws Exception {
        var findPetDto = new PetDto(
                null,
                "Rex",
                1L
        );

        findPetDto = petService.createPet(findPetDto);

        String findPetJson = mockMvc.perform(get("/pets/{petId}", findPetDto.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        var findPetServerDto = objectMapper.readValue(findPetJson, PetDto.class);

        org.assertj.core.api.Assertions.assertThat(findPetDto)
                .usingRecursiveComparison()
                .isEqualTo(findPetServerDto);
    }

    @Test
    void shouldNotFindPetById() throws Exception {
        mockMvc.perform(get("/pets/{petId}", Integer.MAX_VALUE))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

}
