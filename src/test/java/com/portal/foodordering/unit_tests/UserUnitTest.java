package com.portal.foodordering.unit_tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portal.foodordering.exceptions.UserNotFoundException;
import com.portal.foodordering.models.dtos.UserDTO;
import com.portal.foodordering.models.entities.User;
import com.portal.foodordering.repositories.UserRepository;
import com.portal.foodordering.serivces.implementations.UserServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserUnitTest {

    @Mock
    UserRepository userRepository;
    @Mock
    ObjectMapper objectMapper;
    @InjectMocks
    UserServiceImpl userService;

    @Test
    void testCreateUserTestShouldPass() {
        UserDTO createUserDTO = createUserDTO(
                "Bob",
                "Doe",
                "0746544560",
                "bob@gmail.com",
                "Timisoara");

        User user = createUserEntity(
                "Bob",
                "Doe",
                "0746544560",
                "bob@gmail.com",
                "Timisoara"
        );

        User savedUser = new User();
        when(objectMapper.convertValue(createUserDTO, User.class)).thenReturn(user);
        when(objectMapper.convertValue(savedUser, UserDTO.class)).thenReturn(createUserDTO);
        when(userRepository.save(user)).thenReturn(savedUser);

        UserDTO savedUserDTO = userService.createUser(createUserDTO);

        assertEquals(createUserDTO, savedUserDTO);
    }

    @Test
    void testCreatedUserShouldNotPass() {
        UserDTO createUserDTO = createUserDTO(
                "",
                "",
                "0746544560",
                "bob@gmail.com",
                "Timisoara");

        User user = createUserEntity(
                "Bob",
                "Doe",
                "0746544560",
                "bob@gmail.com",
                "Timisoara"
        );

        User savedUser = new User();
        when(objectMapper.convertValue(createUserDTO, User.class)).thenReturn(user);
        when(objectMapper.convertValue(savedUser, UserDTO.class)).thenReturn(createUserDTO);
        when(userRepository.save(user)).thenReturn(savedUser);

        UserDTO savedUserDTO = userService.createUser(createUserDTO);

        assertEquals(createUserDTO, savedUserDTO);
    }


    @Test
    void testGetAllUsersShouldPass() {

        List<User> userList = Arrays.asList(
                createUserEntity("Bob", "Doe", "0746544560", "bob@gmail.com", "Timisoara"),
                createUserEntity("John", "Pop", "0746533260", "john@gmail.com", "Arad")
        );
        when(userRepository.findAll()).thenReturn(userList);

        List<UserDTO> userListDTO = userList.stream()
                .map(user -> objectMapper
                        .convertValue(user, UserDTO.class))
                .collect(Collectors.toList());

        List<UserDTO> result = userService.getAllUsers();

        assertEquals(userListDTO, result);
    }

    @Test
    void testGetAllUsersShouldNotPass() {

        List<User> userList = Arrays.asList(
                createUserEntity("Bob", "Doe", "0746544560", "bob@gmail.com", "Timisoara"),
                createUserEntity("John", "Pop", "0746533260", "john@gmail.com", "Arad")
        );
        when(userRepository.findAll()).thenReturn(userList);

        List<UserDTO> result = userService.getAllUsers();

        assertNotEquals(userList, result);
    }
    @Test
    void testDeleteUserSuccess() {
        Long userId = 1L;

        when(userRepository.existsById(userId)).thenReturn(true);

        String result = userService.deleteUser(userId);
        assertEquals("User " + userId + " successfully deleted!", result);

        Mockito.verify(userRepository).deleteById(userId);
    }

    @Test
    void testFindUserByIdSuccess() {
        Long userId = 1L;
        User existingUser = createUserEntity("Bob", "Doe", "0746544560", "bob@gmail.com", "Timisoara");
        UserDTO userDTO = createUserDTO("Bob", "Doe", "0746544560", "bob@gmail.com", "Timisoara");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        when(objectMapper.convertValue(existingUser, UserDTO.class)).thenReturn(userDTO);

        UserDTO result = userService.findUserById(userId);

        assertEquals(userDTO, result);
    }

    @Test
    void testFindUserByIdNotFound() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.findUserById(userId);
        });
    }

    @Test
    void testFindUserByEmailSuccess() {

        String userEmail = "bob@example.com";
        User existingUser = createUserEntity("Bob", "Doe", "0746544560", "bob@gmail.com", "Timisoara");
        UserDTO userDTO = createUserDTO("Bob", "Doe", "0746544560", "bob@gmail.com", "Timisoara");

        when(userRepository.findUserByEmail(userEmail)).thenReturn(Optional.of(existingUser));
        when(objectMapper.convertValue(existingUser, UserDTO.class)).thenReturn(userDTO);

        UserDTO result = userService.findUserByEmail(userEmail);

        assertEquals(userDTO, result);
    }

    @Test
    void testFindUserByEmailNotFound() {
        String userEmail = "nonexistent@example.com";

        when(userRepository.findUserByEmail(userEmail)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.findUserByEmail(userEmail);
        });
    }

    UserDTO createUserDTO(String fistName, String lastName, String phoneNumber, String email, String address) {
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(fistName);
        userDTO.setLastName(lastName);
        userDTO.setPhoneNumber(phoneNumber);
        userDTO.setEmail(email);
        userDTO.setAddress(address);

        return userDTO;
    }

    User createUserEntity(String fistName, String lastName, String phoneNumber, String email, String address) {
        User user = new User();
        user.setFirstName(fistName);
        user.setLastName(lastName);
        user.setPhoneNumber(phoneNumber);
        user.setEmail(email);
        user.setAddress(address);

        return user;
    }
}
