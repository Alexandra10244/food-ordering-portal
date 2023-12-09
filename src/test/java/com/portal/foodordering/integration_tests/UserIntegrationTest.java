package com.portal.foodordering.integration_tests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.portal.foodordering.models.dtos.UserDTO;
import com.portal.foodordering.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@Transactional
@AutoConfigureTestDatabase
class UserIntegrationTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateUserShouldPass() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setFirstName("Bob");
        userDTO.setLastName("Doe");
        userDTO.setPhoneNumber("0756565643");
        userDTO.setEmail("Bob@test.com");
        userDTO.setAddress("Timisoara");

        MvcResult result = mockMvc.perform(post("/api/users")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andReturn();
        String resultAsString = result.getResponse().getContentAsString();
        UserDTO userDTOConverted = objectMapper.readValue(resultAsString, new TypeReference<UserDTO>() {
        });

        log.info(userDTOConverted.getFirstName() + " user converted.");
        Assertions.assertEquals(userDTOConverted.getFirstName(), userDTO.getFirstName());
        Assertions.assertEquals(userDTOConverted.getLastName(), userDTO.getLastName());
        Assertions.assertEquals(userDTOConverted.getPhoneNumber(), userDTO.getPhoneNumber());
        Assertions.assertEquals(userDTOConverted.getEmail(), userDTO.getEmail());
        Assertions.assertEquals(userDTOConverted.getAddress(), userDTO.getAddress());
    }

    @Test
    void testGetAllUsersShouldPass() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setFirstName("Bob");
        userDTO.setLastName("Doe");
        userDTO.setPhoneNumber("0756565643");
        userDTO.setEmail("Bob@test.com");
        userDTO.setAddress("Timisoara");

        UserDTO userDTO2 = new UserDTO();
        userDTO2.setId(2L);
        userDTO2.setFirstName("Mihai");
        userDTO2.setLastName("Pop");
        userDTO2.setPhoneNumber("0712365643");
        userDTO2.setEmail("pop@test.com");
        userDTO2.setAddress("Brasov");

        List<UserDTO> users = new ArrayList<>();
        users.add(userDTO);
        users.add(userDTO2);

        for (UserDTO userDto : users) {
            mockMvc.perform(post("/api/users")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userDto)))
                    .andExpect(status().isOk());
        }

        MvcResult result = mockMvc.perform(get("/api/users")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();


        String resultAsString = result.getResponse().getContentAsString();
        List<UserDTO> userDTOConvertedList = objectMapper.readValue(resultAsString, new TypeReference<>() {
        });
        userDTOConvertedList.forEach((user -> log.info(user.getFirstName())));

        Assertions.assertEquals(users.size(), userDTOConvertedList.size());
    }
}
